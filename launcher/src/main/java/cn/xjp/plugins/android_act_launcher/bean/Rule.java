package cn.xjp.plugins.android_act_launcher.bean;

import cn.xjp.plugins.android_act_launcher.rule.IntentParam;
import com.intellij.util.xmlb.annotations.AbstractCollection;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Rule {

    private  String name;
    private  String target;
    @AbstractCollection
    private  List<IntentParam> params;

    public Rule(String name, String target, ArrayList<IntentParam> params) {
        this.name = name;
        this.target = target;
        this.params = params;
    }

    public Rule() {
    }

    public String getName() {
        return name;
    }

    public String getTarget() {
        return target;
    }

    public List<IntentParam> getParams() {
        return params;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public void setParams(ArrayList<IntentParam> params) {
        this.params = params;
    }

    @Override
    public String toString() {
        return name+"( "+target+")";
    }

    public void inject(Rule selectedRule) {
        selectedRule.name=this.name;
        selectedRule.target=this.target;
        selectedRule.params=this.params;
    }

}
