package cn.xjp.plugins.android_act_launcher.bean;

import cn.xjp.plugins.android_act_launcher.rule.IntentParam;
import org.jdom.Attribute;
import org.jdom.Element;

import java.util.ArrayList;
import java.util.List;

public class Rule {

    private String name;
    private String target;
    private List<IntentParam> params;

    public Rule(String name, String target, ArrayList<IntentParam> params) {
        this.name = name;
        this.target = target;
        this.params = params;
    }

    public Rule() {
    }

    public static Rule fromXml(Element rule) {
        Rule result = new Rule();
        List<Element> elements = rule.getChildren("option");
        for (Element item : elements) {
            Attribute name = item.getAttribute("name");
            switch (name.getValue()) {
                case "name":
                    Attribute value = item.getAttribute("value");
                    result.name = value.getValue();
                    break;
                case "target":
                    Attribute target = item.getAttribute("value");
                    result.target = target.getValue();
                    break;
                case "params":
                    Element list = item.getChild("list");
                    List<Element> intentParam = list.getChildren("IntentParam");
                    result.params = new ArrayList<>();
                    for (Element paramItem : intentParam) {
                        result.params.add(IntentParam.fromXml(paramItem));
                    }
                    break;
            }
        }
        return result;
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
        return name + "( " + target + ")";
    }

    public void inject(Rule selectedRule) {
        selectedRule.name = this.name;
        selectedRule.target = this.target;
        selectedRule.params = this.params;
    }

    public Element toXmlElement() {
        Element rule = new Element("Rule");
        Element option_name = new Element("option");
        option_name.setAttribute("name", "name");
        option_name.setAttribute("value", name);
        rule.addContent(option_name);

        Element option_target = new Element("option");
        option_target.setAttribute("name", "target");
        option_target.setAttribute("value", target);
        rule.addContent(option_target);

        Element option_params = new Element("option");
        option_params.setAttribute("name", "params");
        Element list = new Element("list");
        for (IntentParam item : params) {
            list.addContent(item.toXmlElement());
        }
        option_params.addContent(list);
        rule.addContent(option_params);

        return rule;
    }
}
