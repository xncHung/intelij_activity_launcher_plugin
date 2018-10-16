package cn.xnc.intellij.plugin.activityanchor.sample.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TestSeri implements Serializable {
    @SerializedName("rawId")
    String id;
    int testInt;
    boolean testBoolean;
}
