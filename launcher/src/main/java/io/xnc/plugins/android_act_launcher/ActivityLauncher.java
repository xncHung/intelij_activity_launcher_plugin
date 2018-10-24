package io.xnc.plugins.android_act_launcher;

import io.xnc.plugins.android_act_launcher.adb.Bridge;
import io.xnc.plugins.android_act_launcher.rule.RemoveRuleDialog;
import io.xnc.plugins.android_act_launcher.rule.Rule;
import io.xnc.plugins.android_act_launcher.rule.AddOrModifyRuleDialog;
import io.xnc.plugins.android_act_launcher.run.LaunchActivityCommand;
import io.xnc.plugins.android_act_launcher.storage.RuleConfigService;
import com.android.builder.model.AndroidProject;
import com.android.ddmlib.IDevice;
import com.android.tools.idea.gradle.project.model.AndroidModuleModel;
import com.android.tools.idea.gradle.project.sync.GradleSyncListener;
import com.android.tools.idea.gradle.project.sync.GradleSyncState;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.JBColor;
import com.intellij.ui.ListCellRendererWrapper;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class ActivityLauncher extends JPanel implements ToolWindowFactory, GradleSyncListener {

    public static final String ID = "ActivityLauncher";
    private JPanel launcherWindowContent;
    private JComboBox<IDevice> devicesBox;
    private JComboBox<Module> moduleBox;
    private JComboBox<String> variantBox;
    private JList<Rule> rulesList;
    private JPanel runActionContainer;
    private JPanel ruleActionContainer;
    private JLabel errorTip;
    private JCheckBox cbClearData;
    private ModuleManager moduleManager;
    private DefaultListModel<Rule> listModel;
    private RuleConfigService configService;
    private DefaultComboBoxModel<Module> moduleModel;

    public ActivityLauncher() {
        super(new BorderLayout());
    }


    // Create the tool window content.
    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        moduleManager = ModuleManager.getInstance(project);
        configService = RuleConfigService.getInstance(project);
        add(launcherWindowContent, BorderLayout.CENTER);
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(this, "", false);
        toolWindow.getContentManager().addContent(content);
//        new FocusWatcher() {
//            @Override
//            protected void focusedComponentChanged(Component component, @Nullable AWTEvent cause) {
//                super.focusedComponentChanged(component, cause);
//                if (component != null && SwingUtilities.isDescendingFrom(component, toolWindow.getComponent())) {
//                    refreshData(project);
//                }
//
//            }
//        }.install(toolWindow.getComponent());
        errorTip.setForeground(JBColor.RED);
        initDeviceBox();
        initModuleBox();
        initVariantBox();
        initRouterList();

        initListeners();

        initRuleActionBar();
        initRunActionBar();

        GradleSyncState.subscribe(project, this);
        refreshData(project);
    }

    private void initRunActionBar() {
        DefaultActionGroup group = (DefaultActionGroup) ActionManager.getInstance().getAction("ActivityLauncher.Run.ToolBar");
        final ActionToolbar actionToolbar = ActionManager.getInstance().createActionToolbar(ActionPlaces.UNKNOWN, group, true);
        actionToolbar.setTargetComponent(runActionContainer);
        actionToolbar.setLayoutPolicy(ActionToolbar.WRAP_LAYOUT_POLICY);
        JComponent component = actionToolbar.getComponent();
        FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT);
        flowLayout.setHgap(5);
        runActionContainer.add(component, flowLayout);
    }

    private void initRuleActionBar() {
        DefaultActionGroup group = (DefaultActionGroup) ActionManager.getInstance().getAction("ActivityLauncher.Rule.ToolBar");
        final ActionToolbar actionToolbar = ActionManager.getInstance().createActionToolbar(ActionPlaces.UNKNOWN, group, true);
        actionToolbar.setTargetComponent(ruleActionContainer);
        actionToolbar.setLayoutPolicy(ActionToolbar.WRAP_LAYOUT_POLICY);
        JComponent component = actionToolbar.getComponent();
        FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT);
        flowLayout.setHgap(5);
        ruleActionContainer.add(component, flowLayout);
    }

    private void initRouterList() {
        rulesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        rulesList.setLayoutOrientation(JList.VERTICAL);
        rulesList.setVisibleRowCount(-1);
        listModel = new DefaultListModel<>();
        rulesList.setModel(listModel);
        rulesList.setCellRenderer(new ListCellRendererWrapper<Rule>() {
            @Override
            public void customize(JList jList, Rule rule, int i, boolean b, boolean b1) {
                setText(rule.toString());
            }

        });
        for (Rule item : configService.rules) {
            listModel.addElement(item);
        }
        cbClearData.setSelected(configService.clearData);
    }

    private void initVariantBox() {
        variantBox.setRenderer(new ListCellRendererWrapper<String>() {
            @Override
            public void customize(JList list, String value, int index, boolean selected, boolean hasFocus) {
                setText(value);
            }

        });
    }

    private void initListeners() {
        moduleBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                Object item = e.getItem();
                if (item instanceof Module) {
                    Module module = (Module) item;
                    refreshVariantBox(module);
                }
            }
        });

        cbClearData.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                configService.clearData = cbClearData.isSelected();
            }
        });
    }

    public void openAddRuleDialog(Project project) {
        openRuleDialog(project, null);
    }

    private void openRuleDialog(Project project, Rule selectedValue) {
        Module module = (Module) moduleModel.getSelectedItem();
        if (module == null) {
            showError("select a module");
            return;
        }
        AddOrModifyRuleDialog addNewRuleDialog = new AddOrModifyRuleDialog(project, module, this, selectedValue);
        addNewRuleDialog.setSize(1000, 500);
        addNewRuleDialog.show();
    }

    private void refreshVariantBox(Module module) {
        AndroidModuleModel androidModuleModel = AndroidModuleModel.get(module);
        if (androidModuleModel != null) {
            Collection<String> variantNames = androidModuleModel.getVariantNames();
            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(new Vector<>(variantNames));
            variantBox.setModel(model);
            if (model.getSize() > 0) {
                variantBox.setSelectedIndex(0);
            }
        }
    }

    public void refreshData(@NotNull Project project) {
        IDevice[] deviceList = Bridge.getDeviceList(project);
        devicesBox.setModel(new DefaultComboBoxModel<>(deviceList));

        Module[] modules = moduleManager.getModules();
        Vector<Module> apps = new Vector<>();
        for (Module module : modules) {
            AndroidModuleModel androidModuleModel = AndroidModuleModel.get(module);
            if (androidModuleModel != null && androidModuleModel.getAndroidProject().getProjectType() == AndroidProject.PROJECT_TYPE_APP) {
                apps.add(module);
            }
        }
        moduleModel = new DefaultComboBoxModel<>(apps);
        moduleBox.setModel(moduleModel);
        if (moduleModel.getSize() >= 1) {
            moduleBox.setSelectedIndex(0);
            refreshVariantBox(moduleModel.getElementAt(0));
        }
    }

    private void initModuleBox() {
        moduleBox.setRenderer(new ListCellRendererWrapper<Module>() {
            @Override
            public void customize(JList list, Module value, int index, boolean selected, boolean hasFocus) {
                setText(value == null ? "No App Module Found" : value.getName());
            }
        });
    }

    private void initDeviceBox() {

        devicesBox.setRenderer(new ListCellRendererWrapper<IDevice>() {
            @Override
            public void customize(JList list, IDevice value, int index, boolean selected, boolean hasFocus) {
                setText(value == null ? "No Device Found" : value.getName());
            }
        });
    }

    @Override
    public boolean shouldBeAvailable(@NotNull Project project) {
        return true;
    }

    @Override
    public void syncStarted(@NotNull Project project, boolean b, boolean b1) {

    }

    @Override
    public void setupStarted(@NotNull Project project) {

    }

    @Override
    public void syncSucceeded(@NotNull Project project) {
        refreshData(project);
    }

    @Override
    public void syncFailed(@NotNull Project project, @NotNull String s) {
    }

    @Override
    public void syncSkipped(@NotNull Project project) {

    }

    public void addRule(Rule rule) {
        listModel.add(0, rule);
        configService.addRule(0, rule);
    }

    public void refreshRules() {
        rulesList.updateUI();
    }

    public void removeRule() {
        RemoveRuleDialog ruleDialog = new RemoveRuleDialog(null, new RemoveRuleDialog.CallBack() {
            @Override
            public void onCancelAction() {

            }

            @Override
            public void onOKAction() {
                int selectedValue = rulesList.getSelectedIndex();
                if (selectedValue >= 0) {
                    listModel.remove(selectedValue);
                    configService.removeRule(selectedValue);
                }
            }
        });
        ruleDialog.show();
    }

    public void openEditRuleDialog(Project project) {
        Rule selectedValue = rulesList.getSelectedValue();
        if (selectedValue == null) {
            return;
        }
        openRuleDialog(project, selectedValue);
    }

    public void submitRun(Project project, boolean debug) {
        Object selectedItem = devicesBox.getSelectedItem();
        if (!(selectedItem instanceof IDevice)) {
            showError("no Device selected!");
            return;
        }
        Object boxSelectedItem = moduleBox.getSelectedItem();
        if (!(boxSelectedItem instanceof Module)) {
            showError("no Module selected!");
            return;
        }
        Rule selectedRule = getSelectedRule();
        if (selectedRule == null) {
            showError("select a target to launch");
        }
        IDevice device = (IDevice) selectedItem;
        Module module = (Module) boxSelectedItem;

        new LaunchActivityCommand(selectedRule, debug, cbClearData.isSelected()).apply(project, device, module);
    }

    private void showError(String msg) {
        errorTip.setText(msg);
        new Timer(2000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                errorTip.setText("");
            }
        }).start();
    }

    public Rule getSelectedRule() {
        return rulesList.getSelectedValue();
    }

    public IDevice getSelectedDevice() {
        Object selectedItem = devicesBox.getSelectedItem();
        if (selectedItem instanceof IDevice) {
            return (IDevice) selectedItem;
        }
        return null;
    }

    public Module getSelectedModule() {
        Object selectedItem = moduleBox.getSelectedItem();
        if (selectedItem instanceof Module) {
            return (Module) selectedItem;
        }
        return null;
    }


    public String getSelectedVariant() {
        Object selectedItem = variantBox.getSelectedItem();
        if (selectedItem instanceof String) {
            return (String) selectedItem;
        }
        return null;
    }

}
