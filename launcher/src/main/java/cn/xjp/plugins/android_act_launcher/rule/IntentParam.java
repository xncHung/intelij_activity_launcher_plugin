package cn.xjp.plugins.android_act_launcher.rule;

import org.jdom.Attribute;
import org.jdom.DataConversionException;
import org.jdom.Element;

import java.util.List;

public class IntentParam {
    static final String[] COLUMN_NAMES = new String[]{"active", "Key", "Type", "RealTypePath", "JsonOfValue"};
    static final String[] TYPE_NAMES = new String[]{
            "String or String array",
            "int or int array",
            "Serializable",
            "boolean or boolean array",
            "byte or byte array",
            "char or char array",
//            "charSequence or charSequence array",
            "float or float array",
            "double or double array",
            "long or long array",
            "short or short array",
            "Parcelable or Parcelable array",
            "Parcelable ArrayList",
            "Integer ArrayList",
            "String ArrayList",
//            "charSequence ArrayList",
    };
    private boolean isActive = true;
    private String key;
    private String type;
    private String value;
    private String realType = "PrimitiveOrString";

    IntentParam() {
    }

    IntentParam(boolean isActive, String key, String type, String realType, String value) {
        this.isActive = isActive;
        this.key = key;
        this.type = type;
        this.value = value;
        this.realType = realType;
    }

    public static IntentParam fromXml(Element paramItem) {
        IntentParam intentParam = new IntentParam();
        List<Element> elements = paramItem.getChildren("option");
        for (Element item : elements) {
            Attribute name = item.getAttribute("name");
            Attribute value = item.getAttribute("value");
            switch (name.getValue()) {
                case "isActive":
                    try {
                        intentParam.isActive = value.getBooleanValue();
                    } catch (DataConversionException e) {
                        e.printStackTrace();
                        intentParam.isActive = false;
                    }
                    break;

                case "key":
                    intentParam.key = value.getValue();
                    break;
                case "type":
                    intentParam.type = value.getValue();
                    break;
                case "realType":
                    intentParam.realType = value.getValue();
                    break;
                case "value":
                    intentParam.value = value.getValue();
                    break;

            }
        }
        return intentParam;
    }
    Object[] toRowColumns() {
        return new Object[]{isActive, key, type, realType, value};
    }

    public Element toXmlElement() {
        Element intentParam = new Element("IntentParam");

        Element option_name = new Element("option");
        option_name.setAttribute("name", "isActive");
        option_name.setAttribute("value", String.valueOf(isActive));
        intentParam.addContent(option_name);


        Element option_key = new Element("option");
        option_key.setAttribute("name", "key");
        option_key.setAttribute("value", key);
        intentParam.addContent(option_key);


        Element option_type = new Element("option");
        option_type.setAttribute("name", "type");
        option_type.setAttribute("value", type);
        intentParam.addContent(option_type);

        Element option_realType = new Element("option");
        option_realType.setAttribute("name", "realType");
        option_realType.setAttribute("value", realType);
        intentParam.addContent(option_realType);

        Element option_value = new Element("option");
        option_value.setAttribute("name", "value");
        option_value.setAttribute("value", value);
        intentParam.addContent(option_value);

        return intentParam;
    }
}
