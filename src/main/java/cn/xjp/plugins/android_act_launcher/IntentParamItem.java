package cn.xjp.plugins.android_act_launcher;

import cn.xjp.plugins.android_act_launcher.rule.IntentParam;

import javax.swing.*;
import java.awt.*;

public class IntentParamItem {
    private JTextField tfKey;
    private JComboBox<String> cbbType;
    private JTextField tfValue;
    private JCheckBox cbActive;
    private JPanel itemContent;
    private static final String[] TYPES=new String[]{"String","int","long","boolean","float",};

    public Component render(IntentParam value) {
        tfKey.setText("Key");
        tfValue.setText("value,use json to represent a object");
        cbbType.setModel(new DefaultComboBoxModel<>(TYPES));
//        cbbType.setRenderer(new ListCellRendererWrapper<String>() {
//            @Override
//            public void customize(JList jList, String s, int i, boolean b, boolean b1) {
//
//            }
//
//            @Override
//            public Component getListCellRendererComponent(JList<? extends String> list, String value, int index, boolean isSelected, boolean cellHasFocus) {
//                return null;
//            }
//        });
        return itemContent;
    }
}
