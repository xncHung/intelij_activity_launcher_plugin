package cn.xjp.plugins.android_act_launcher.bean;

import cn.xjp.plugins.android_act_launcher.rule.IntentParam;

import java.util.ArrayList;
import java.util.List;

public class Rule {

    private  String name;
    private  String target;
    private  ArrayList<IntentParam> params;

    public Rule(String name, String target, ArrayList<IntentParam> params) {
        this.name = name;
        this.target = target;
        this.params = params;
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
