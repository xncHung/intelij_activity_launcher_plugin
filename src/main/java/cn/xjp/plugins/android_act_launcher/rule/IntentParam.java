package cn.xjp.plugins.android_act_launcher.rule;

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

    public IntentParam() {
    }

    public IntentParam(boolean isActive, String key, String type, String realType, String value) {
        this.isActive = isActive;
        this.key = key;
        this.type = type;
        this.value = value;
        this.realType = realType;
    }

    Object getValueColumn(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return isActive;
            case 1:
                return key;
            case 2:
                return type;
            case 3:
                return realType;
            case 4:
                return value;
            default:
                return null;
        }
    }

    public boolean isActive() {
        return isActive;
    }

    public String getKey() {
        return key;
    }

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    Object[] toRowColumns() {
        return new Object[]{isActive, key, type, realType, value};
    }
}
