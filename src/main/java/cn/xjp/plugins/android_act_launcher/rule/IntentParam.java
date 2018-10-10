package cn.xjp.plugins.android_act_launcher.rule;

public class IntentParam {
    static final String[] COLUMN_NAMES = new String[]{"active", "Key", "Type", "Value"};
    private boolean isActive;
    private String key;
    private String type;
    private String value;

    Object getValueColumn(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return isActive;
            case 1:
                return key;
            case 2:
                return type;
            case 3:
                return value;
            default:
                return null;
        }
    }

    public Object[] toRowClonums() {
        return new Object[]{isActive, key, type, value};
    }
}
