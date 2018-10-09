package cn.xjp.plugins.android_act_launcher;

import android.os.Parcelable;
import cn.xjp.plugins.android_act_launcher.bean.IntentParam;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Type;

public class IntentParamItem {
    private JTextField tfKey;
    private JComboBox<Type> cbbType;
    private JTextField tfValue;
    private JCheckBox cbActive;
    private JPanel itemContent;
    private static final String[] TYPES=new String[]{"String","int","long","boolean","float",};

    public Component render(IntentParam value) {
        tfKey.setText("Key");
        tfValue.setText("value,use json to present a object");
        cbbType.setModel(new );
//        return null;
    }
}
