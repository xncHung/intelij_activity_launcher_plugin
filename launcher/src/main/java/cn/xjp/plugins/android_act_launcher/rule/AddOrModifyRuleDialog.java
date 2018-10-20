package cn.xjp.plugins.android_act_launcher.rule;

import cn.xjp.plugins.android_act_launcher.ActivityLauncher;
import cn.xjp.plugins.android_act_launcher.bean.Rule;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.JBColor;
import org.apache.http.util.TextUtils;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.*;
import java.util.ArrayList;

public class AddOrModifyRuleDialog extends JFrame {
    private final ActivityLauncher activityLauncher;
    private final Rule selectedRule;
    private JTextField targetAct;
    private JButton addNewParam;
    private JButton removeParam;
    private JButton okBtn;
    private JButton cancel;
    private JTextField ruleName;
    private JPanel rootContent;
    private JTable paramTable;
    private JLabel errorTip;
    private ParamItemModel dataModel;

    public AddOrModifyRuleDialog(ActivityLauncher activityLauncher, Rule selectedValue) {
        setContentPane(rootContent);
        this.selectedRule = selectedValue;
        setAlwaysOnTop(true);
        this.activityLauncher = activityLauncher;
        getRootPane().setDefaultButton(okBtn);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });
        okBtn.addActionListener(e -> onOkBtn());
        cancel.addActionListener(e -> onCancel());
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
    }

    private void initData() {
        ruleName.setText(selectedRule.getName());
        targetAct.setText(selectedRule.getTarget());
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
        paramTable.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(comboBox));

    }

    private void onOkBtn() {
        Rule rule = generateRule();
        if (rule != null) {
            if (selectedRule == null) {
                activityLauncher.addRule(rule);
            } else {
                rule.inject(selectedRule);
                activityLauncher.refreshRules();
            }
            dispose();
        }
    }

    private Rule generateRule() {
        String name = ruleName.getText();
        if (TextUtils.isEmpty(name)) {
            showError("please input Name");
            return null;
        }
        String path = targetAct.getText();
        if (TextUtils.isEmpty(path) || !checkActPath(path)) {
            showError("target Activity is empty or not exist");
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
        return true;
    }

    private boolean checkActPath(String path) {
        return true;
// TODO: 2018/10/13 check target path
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

    private void onCancel() {
        dispose();
    }

}
