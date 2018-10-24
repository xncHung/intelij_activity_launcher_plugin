package io.xnc.plugins.android_act_launcher.storage;

import io.xnc.plugins.android_act_launcher.rule.Rule;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import org.jdom.Attribute;
import org.jdom.DataConversionException;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@State(name = "ActivityLauncherRules",
        storages = {@Storage("ActivityLauncherRules.xml")})
public class RuleConfigService implements PersistentStateComponent<Element> {
    public List<Rule> rules = new ArrayList<>();
    public boolean clearData = true;

    @Nullable
    @Override
    public Element getState() {
        Element config = new Element("config");
        Element option_stopApp = new Element("option");
        option_stopApp.setAttribute("name", "clearData");
        option_stopApp.setAttribute("value", String.valueOf(clearData));
        config.addContent(option_stopApp);


        Element option_rules = new Element("option");
        option_rules.setAttribute("name", "rules");
        Element list = new Element("list");
        for (Rule item : rules) {
            list.addContent(item.toXmlElement());
        }
        option_rules.addContent(list);
        config.addContent(option_rules);
        return config;
    }

    @Override
    public void loadState(@NotNull Element state) {
        List<Element> elements = state.getChildren("option");
        for (Element item : elements) {
            Attribute name = item.getAttribute("name");
            switch (name.getValue()) {
                case "clearData":
                    Attribute value = item.getAttribute("value");
                    try {
                        clearData = value.getBooleanValue();
                    } catch (DataConversionException e) {
                        clearData = true;
                    }
                    break;
                case "rules":
                    Element list = item.getChild("list");
                    List<Element> rule = list.getChildren("Rule");
                    for (Element ruleItem : rule) {
                        rules.add(Rule.fromXml(ruleItem));
                    }
                    break;
            }
        }
    }


    public static RuleConfigService getInstance(Project project) {
        return ServiceManager.getService(project, RuleConfigService.class);
    }

    public void addRule(int i, Rule rule) {
        rules.add(i, rule);
    }

    public void removeRule(int selectedValue) {
        rules.remove(selectedValue);
    }
}
