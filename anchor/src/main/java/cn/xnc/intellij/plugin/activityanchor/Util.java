package cn.xnc.intellij.plugin.activityanchor;

import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import java.io.Serializable;


public class Util {
    public static boolean isArray(String value) {
        try {
            JsonElement parse = new JsonParser().parse(value);
            return parse.isJsonArray();
        } catch (JsonSyntaxException e) {
            return false;
        }
    }

    static boolean checkSerializable(Class<?> aClass) {
        Class<?>[] interfaces = aClass.getInterfaces();
        for (Class item : interfaces) {
            if (item.equals(Serializable.class)) return true;
        }
        return false;
    }

    public static boolean checkParcelable(Class realType) {
        Class<?>[] interfaces = realType.getInterfaces();
        for (Class item : interfaces) {
            if (item.equals(Parcelable.class)) return true;
        }
        return false;
    }
}
