package io.xnc.plugins.android_act_launcher.rule;

import android.os.Parcelable;
import io.xnc.plugins.android_act_launcher.ActivityLauncher;
import com.android.tools.idea.run.activity.ActivityLocatorUtils;
import com.intellij.codeInsight.daemon.DaemonCodeAnalyzer;
import com.intellij.ide.util.TreeClassChooser;
import com.intellij.ide.util.TreeClassChooserFactory;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.fileTypes.PlainTextLanguage;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.ComponentWithBrowseButton;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import com.intellij.psi.search.ProjectScope;
import com.intellij.ui.EditorTextField;
import com.intellij.ui.JBColor;
import com.intellij.ui.LanguageTextField;
import org.apache.http.util.TextUtils;
import org.jetbrains.android.util.AndroidBundle;
import org.jetbrains.android.util.AndroidUtils;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumnModel;
import java.awt.event.*;
import java.io.Serializable;
import java.util.ArrayList;

public class AddOrModifyRuleDialog extends DialogWrapper {
    private final ActivityLauncher activityLauncher;
    private final Rule selectedRule;
    private final Project project;
    private final Module module;
    private JButton addNewParam;
    private JButton removeParam;
    private JTextField ruleName;
    private JPanel rootContent;
    private JTable paramTable;
    private JLabel errorTip;
    private ComponentWithBrowseButton<EditorTextField> actSelector;
    private ParamItemModel dataModel;

    public AddOrModifyRuleDialog(Project project, Module module, ActivityLauncher activityLauncher, Rule selectedValue) {
        super(project, true);
        this.project = project;
        this.module = module;
        this.selectedRule = selectedValue;
        this.activityLauncher = activityLauncher;
        initParamList();
        removeParam.setEnabled(false);
        errorTip.setForeground(JBColor.RED);
        addNewParam.addActionListener(e -> {
            addNewParam();
        });

        removeParam.addActionListener(e -> {
            removeNewParam();
        });
        paramTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int selectedRow = paramTable.getSelectedRow();
                removeParam.setEnabled(selectedRow >= 0);
            }
        });
        if (this.selectedRule != null) {
            initData();
        }

        actSelector.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!project.isInitialized()) {
                    return;
                }
                final JavaPsiFacade facade = JavaPsiFacade.getInstance(project);
                PsiClass activityBaseClass = facade.findClass(AndroidUtils.ACTIVITY_BASE_CLASS_NAME, ProjectScope.getAllScope(project));
                if (activityBaseClass == null) {
                    Messages.showErrorDialog(project, AndroidBundle.message("cant.find.activity.class.error"), " Select Activity");
                    return;
                }
                PsiClass initialSelection =
                        facade.findClass(actSelector.getChildComponent().getText(), module.getModuleWithDependenciesScope());
                TreeClassChooser chooser = TreeClassChooserFactory.getInstance(project)
                        .createInheritanceClassChooser("Select Activity Class", module.getModuleWithDependenciesScope(), activityBaseClass,
                                initialSelection, null);
                chooser.showDialog();
                PsiClass selClass = chooser.getSelected();
                if (selClass != null) {
                    actSelector.getChildComponent().setText(ActivityLocatorUtils.getQualifiedActivityName(selClass));
                }
            }
        });
        init();
        setTitle((selectedValue != null ? "Modify" : "Add New") + " Activity Launch");
    }

    private void initData() {
        ruleName.setText(selectedRule.getName());
        actSelector.getChildComponent().setText(selectedRule.getTarget());
        if (selectedRule.getParams() != null) {
            for (IntentParam item : selectedRule.getParams()) {
                dataModel.addRow(item.toRowColumns());
            }
        }
    }

    private void removeNewParam() {
        dataModel.removeRow(paramTable.getSelectedRow());
    }

    private void addNewParam() {
        dataModel.addRow(new IntentParam().toRowColumns());
    }

    private void initParamList() {
        paramTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        paramTable.setCellSelectionEnabled(false);
        paramTable.setRowSelectionAllowed(true);
        dataModel = new ParamItemModel();
        paramTable.setModel(dataModel);
        ComboBox<String> comboBox = new ComboBox<>(new DefaultComboBoxModel<>(IntentParam.TYPE_NAMES));
        TableColumnModel columnModel = paramTable.getColumnModel();
        columnModel.getColumn(2).setCellEditor(new DefaultCellEditor(comboBox));
        paramTable.setRowHeight(25);
        columnModel.getColumn(0).setMaxWidth(60);
        columnModel.getColumn(1).setMaxWidth(120);
        columnModel.getColumn(2).setMaxWidth(180);
        columnModel.getColumn(3).setMaxWidth(400);
    }

    @Override
    protected void doOKAction() {
        Rule rule = generateRule();
        if (rule != null) {
            if (selectedRule == null) {
                activityLauncher.addRule(rule);
            } else {
                rule.inject(selectedRule);
                activityLauncher.refreshRules();
            }
            super.doOKAction();
        }
    }

    private Rule generateRule() {
        String name = ruleName.getText();
        if (TextUtils.isEmpty(name)) {
            showError("please input Name");
            return null;
        }
        String path = actSelector.getChildComponent().getText();
        if (TextUtils.isEmpty(path) || !checkActPath(path)) {
            showError("target Activity is incorrect or not exist");
            return null;
        }
        ArrayList<IntentParam> params = new ArrayList<>();
        for (int index = 0; index < paramTable.getRowCount(); index++) {
            boolean isActive = (boolean) paramTable.getModel().getValueAt(index, 0);
            String key = (String) paramTable.getModel().getValueAt(index, 1);
            String type = (String) paramTable.getModel().getValueAt(index, 2);
            String realType = (String) paramTable.getModel().getValueAt(index, 3);
            String value = (String) paramTable.getModel().getValueAt(index, 4);
            if (isActive) {
                if (TextUtils.isEmpty(key)) {
                    showError("please input Key");
                    return null;
                }
                if (TextUtils.isEmpty(type)) {
                    showError("please input Type");
                    return null;
                }
                if (TextUtils.isEmpty(value)) {
                    showError("please input Value");
                    return null;
                }
                params.add(new IntentParam(true, key, type, realType, value));
            } else if (!TextUtils.isEmpty(key) ||
                    !TextUtils.isEmpty(type) ||
                    !TextUtils.isEmpty(value) ||
                    (!TextUtils.isEmpty(realType) && checkTypePath(realType))
            ) {
                params.add(new IntentParam(false, key, type, realType, value));
            }
        }
        return new Rule(name, path, params);
    }

    private boolean checkTypePath(String realType) {
        if (Constant.PRIMITIVE_OR_STRING.equals(realType)) {
            return true;
        }
        JavaPsiFacade facade = JavaPsiFacade.getInstance(project);
        PsiClass aClass = facade.findClass(realType, module.getModuleWithDependenciesAndLibrariesScope(false));
        PsiClass parce = facade.findClass(Parcelable.class.getName(), ProjectScope.getAllScope(project));
        PsiClass seri = facade.findClass(Serializable.class.getName(), ProjectScope.getAllScope(project));
        if (aClass == null) {
            showError("class:" + realType + " is not exist!");
            return false;
        }
        if (aClass.hasTypeParameters() || aClass.isInterface()) {
            showError("Interface or Parameterized Type is not support!");
            return false;
        }
        if ((parce == null || !aClass.isInheritor(parce, false)) && (seri == null || !aClass.isInheritor(seri, false))) {
            showError("realType must be Parcelable or Serializable! ");
            return false;
        }
        return true;
    }

    private boolean checkActPath(String path) {
        JavaPsiFacade facade = JavaPsiFacade.getInstance(project);
        PsiClass aClass = facade.findClass(path, ProjectScope.getAllScope(project));
        PsiClass parent = facade.findClass(AndroidUtils.ACTIVITY_BASE_CLASS_NAME, ProjectScope.getAllScope(project));
        return aClass != null && parent != null && !aClass.isInheritor(parent, false);

    }

    private void showError(String msg) {
        errorTip.setText(msg);
        new Timer(3000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                errorTip.setText("");
            }
        }).start();
    }


    private void createUIComponents() {
        final EditorTextField editorTextField = new LanguageTextField(PlainTextLanguage.INSTANCE, project, "") {
            @Override
            protected EditorEx createEditor() {
                final EditorEx editor = super.createEditor();
                final PsiFile file = PsiDocumentManager.getInstance(project).getPsiFile(editor.getDocument());

                if (file != null) {
                    DaemonCodeAnalyzer.getInstance(project).setHighlightingEnabled(file, false);
                }
                return editor;
            }
        };
        actSelector = new ComponentWithBrowseButton<EditorTextField>(editorTextField, null);
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return rootContent;
    }


}
