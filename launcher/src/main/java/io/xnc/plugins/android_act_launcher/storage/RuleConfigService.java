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

import java.util.*;

@State(name = "ActivityLauncherRules",
        storages = {@Storage("ActivityLauncherRules.xml")})
public class RuleConfigService implements PersistentStateComponent<Element> {
    public List<Rule> rules = new ArrayList<>();
    public boolean clearData;
    public String selectedDeviceId = "";
    public String selectedModule = "";
    public HashMap<String, String> selectedProductVariantMap = new HashMap<>();

    @Nullable
    @Override
    public Element getState() {
        Element config = new Element("config");

        addField2Config(config, "clearData", String.valueOf(clearData));
        addField2Config(config, "selectedDeviceId", selectedDeviceId);
        addField2Config(config, "selectedModule", selectedModule);

        addMap2Config(config, "selectedProductVariantMap", this.selectedProductVariantMap);

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

    private void addMap2Config(Element config, String name, Map<String, String> map) {
        Element element = new Element("option");
        element.setAttribute("name", name);
        Element mapElement = new Element("map");
        for (Map.Entry<String, String> entry : map.entrySet()) {
            addEntry2Map(mapElement, entry);
        }
        element.addContent(mapElement);
        config.addContent(element);
    }

    private void addEntry2Map(Element config, Map.Entry<String, String> entry) {
        Element element = new Element("entry");
        element.setAttribute("key", entry.getKey());
        element.setAttribute("value", entry.getValue());
        config.addContent(element);
    }

    private void addField2Config(Element config, String name, String value) {
        Element element = new Element("option");
        element.setAttribute("name", name);
        element.setAttribute("value", value);
        config.addContent(element);
    }

    @Override
    public void loadState(@NotNull Element state) {
        List<Element> elements = state.getChildren("option");
        for (Element item : elements) {
            Attribute name = item.getAttribute("name");
            Attribute value;
            switch (name.getValue()) {
                case "clearData":
                    value = item.getAttribute("value");
                    try {
                        clearData = value.getBooleanValue();
                    } catch (DataConversionException e) {
                        clearData = false;
                    }
                    break;
                case "selectedDeviceId":
                    value = item.getAttribute("value");
                    selectedDeviceId = value.getValue();
                    break;
                case "selectedModule":
                    value = item.getAttribute("value");
                    selectedModule = value.getValue();
                    break;
                case "rules":
                    Element list = item.getChild("list");
                    List<Element> rule = list.getChildren("Rule");
                    for (Element ruleItem : rule) {
                        rules.add(Rule.fromXml(ruleItem));
                    }
                    break;
                case "selectedProductVariantMap":
                    Element map = item.getChild("map");
                    List<Element> entrys = map.getChildren("entry");
                    for (Element element : entrys) {
                        selectedProductVariantMap.put(element.getAttributeValue("key"), element.getAttributeValue("value"));
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
