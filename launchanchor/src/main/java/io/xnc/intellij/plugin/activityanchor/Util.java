package io.xnc.intellij.plugin.activityanchor;

import android.os.Parcelable;

import java.io.Serializable;


class Util {
    static boolean checkSerializable(Class<?> aClass) {
        Class<?>[] interfaces = aClass.getInterfaces();
        for (Class item : interfaces) {
            if (item.equals(Serializable.class)) return true;
        }
        return false;
    }

    static boolean checkParcelable(Class realType) {
        Class<?>[] interfaces = realType.getInterfaces();
        for (Class item : interfaces) {
            if (item.equals(Parcelable.class)) return true;
        }
        return false;
    }
}
