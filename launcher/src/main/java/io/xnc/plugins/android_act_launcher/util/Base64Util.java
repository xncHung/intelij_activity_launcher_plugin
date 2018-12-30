package io.xnc.plugins.android_act_launcher.util;


import com.intellij.util.Base64;

import java.nio.charset.StandardCharsets;

public class Base64Util {
    public static String encode(String src) {
        return Base64.encode(src.getBytes(StandardCharsets.UTF_8));
    }
}
