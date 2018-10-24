package io.xnc.intellij.plugin.activityanchor.sample.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TestSeri implements Serializable {
    @SerializedName("rawId")
    String id;
    int testInt;
    boolean testBoolean;

    @Override
    public String toString() {
        return "TestSeri{" +
                "id='" + id + '\'' +
                ", testInt=" + testInt +
                ", testBoolean=" + testBoolean +
                '}';
    }
}
