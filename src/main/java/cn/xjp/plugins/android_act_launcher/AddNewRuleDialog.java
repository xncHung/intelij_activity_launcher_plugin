package cn.xjp.plugins.android_act_launcher;

import cn.xjp.plugins.android_act_launcher.bean.IntentParam;
import com.intellij.icons.AllIcons;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AddNewRuleDialog extends JFrame {
    private JTextField targetAct;
    private JLabel addNewParam;
    private JLabel removeParam;
    private JButton okBtn;
    private JButton cancel;
    private JTextField ruleName;
    private JPanel rootContent;
    private JList<IntentParam> paramsList;
    private DefaultListModel<IntentParam> paramsModel;

    public AddNewRuleDialog() {
        setContentPane(rootContent);
        setAlwaysOnTop(true);
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
        addNewParam.setIcon(AllIcons.General.Add);
        addNewParam.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                addNewParam();
            }
        });
    }

    private void addNewParam() {

    }

    private void initParamList() {
        paramsModel = new DefaultListModel<>();
        paramsList.setModel(paramsModel);
        paramsList.setCellRenderer(new ListCellRenderer<IntentParam>() {
            @Override
            public Component getListCellRendererComponent(JList<? extends IntentParam> list, IntentParam value, int index, boolean isSelected, boolean cellHasFocus) {
                if (value != null) {
                    return new IntentParamItem().render(value);
                }
                return null;
            }
        });
    }

    private void onOkBtn() {
        dispose();
    }

    private void onCancel() {
        dispose();
    }
}
