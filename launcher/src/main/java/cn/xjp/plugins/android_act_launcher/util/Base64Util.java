package cn.xjp.plugins.android_act_launcher.util;


import com.intellij.util.Base64;

public class Base64Util {
    public static String encode(String src) {
        return Base64.encode(src.getBytes());
    }
}
