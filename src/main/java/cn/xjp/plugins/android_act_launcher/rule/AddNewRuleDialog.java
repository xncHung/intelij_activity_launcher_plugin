package cn.xjp.plugins.android_act_launcher.rule;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.ui.ComboBox;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.Array;

public class AddNewRuleDialog extends JFrame {
    private JTextField targetAct;
    private JLabel addNewParam;
    private JLabel removeParam;
    private JButton okBtn;
    private JButton cancel;
    private JTextField ruleName;
    private JPanel rootContent;
    private JTable paramTable;
    private ParamItemModel dataModel;

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
        dataModel.addRow(new IntentParam().toRowClonums());
    }

    private void initParamList() {
        paramTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        paramTable.setCellSelectionEnabled(false);
        paramTable.setRowSelectionAllowed(true);
        paramTable.setDefaultRenderer(Array.class, new TableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JComboBox<String> comboBox = new JComboBox<>(new String[]{
                        "String",
                        "int",
                        "long",
                        "float",
                        "boolean",
                        "Serialaziable",
                });
                comboBox.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        dataModel.setValueAt(comboBox.getSelectedItem(), row, column);
                    }
                });
                return comboBox;
            }
        });
        dataModel = new ParamItemModel();
        paramTable.setModel(dataModel);
    }

    private void onOkBtn() {
        dispose();
    }

    private void onCancel() {
        dispose();
    }

}
