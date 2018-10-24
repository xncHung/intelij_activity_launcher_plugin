package io.xnc.intellij.plugin.activityanchor;

public class ParamItem {
    static final String STRING_OR_STRING_ARRAY = "String or String array";
    static final String INT_OR_INT_ARRAY = "int or int array";
    static final String SERIALIZABLE = "Serializable";
    static final String BOOLEAN_OR_BOOLEAN_ARRAY = "boolean or boolean array";
    static final String BYTE_OR_BYTE_ARRAY = "byte or byte array";
    static final String CHAR_OR_CHAR_ARRAY = "char or char array";
    //  public static final String CHAR_SEQUENCE_OR_CHAR_SEQUENCE_ARRAY = "charSequence or charSequence array";
    static final String FLOAT_OR_FLOAT_ARRAY = "float or float array";
    static final String DOUBLE_OR_DOUBLE_ARRAY = "double or double array";
    static final String LONG_OR_LONG_ARRAY = "long or long array";
    static final String SHORT_OR_SHORT_ARRAY = "short or short array";
    static final String PARCELABLE_OR_PARCELABLE_ARRAY = "Parcelable or Parcelable array";
    static final String PARCELABLE_ARRAY_LIST = "Parcelable ArrayList";
    static final String INTEGER_ARRAY_LIST = "Integer ArrayList";
    static final String STRING_ARRAY_LIST = "String ArrayList";
    //  public static final String CHAR_SEQUENCE_ARRAY_LIST = "charSequence ArrayList";
    static final String BUNDLE_WITH_KEY = "Bundle with Key";
    static final String BUNDLE_WITHOUT_KEY = "Bundle without Key";
    static final String INTENT = "Intent";
    static final String[] TYPE_NAMES = new String[]{
            STRING_OR_STRING_ARRAY,
            INT_OR_INT_ARRAY,
            SERIALIZABLE,
            BOOLEAN_OR_BOOLEAN_ARRAY,
            BYTE_OR_BYTE_ARRAY,
            CHAR_OR_CHAR_ARRAY,
//            CHAR_SEQUENCE_OR_CHAR_SEQUENCE_ARRAY,
            FLOAT_OR_FLOAT_ARRAY,
            DOUBLE_OR_DOUBLE_ARRAY,
            LONG_OR_LONG_ARRAY,
            SHORT_OR_SHORT_ARRAY,
            PARCELABLE_OR_PARCELABLE_ARRAY,
            PARCELABLE_ARRAY_LIST,
            INTEGER_ARRAY_LIST,
            STRING_ARRAY_LIST,
//            CHAR_SEQUENCE_ARRAY_LIST,
            BUNDLE_WITH_KEY,
            BUNDLE_WITHOUT_KEY,
            INTENT
    };
    private String key;
    private String type;
    private String value;
    private String realType;

    String getRealType() {
        return realType;
    }

    String getKey() {
        return key;
    }

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }
}
