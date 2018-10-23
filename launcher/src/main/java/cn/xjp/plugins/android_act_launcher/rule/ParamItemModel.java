package cn.xjp.plugins.android_act_launcher.rule;

import cn.xjp.plugins.android_act_launcher.ActivityLauncher;
import com.intellij.openapi.diagnostic.Logger;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

class ParamItemModel extends DefaultTableModel {


    ParamItemModel() {
        super(IntentParam.COLUMN_NAMES, 0);
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        Object value = getValueAt(0, columnIndex);
        return value == null ? Object.class : value.getClass();
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if (columnIndex == 3) {
            Object valueAt = getValueAt(rowIndex, 2);
            if (valueAt == null) {
                return false;
            }
            String s = valueAt.toString();
            return s.startsWith("Parcelable") || s.startsWith("Serializable");
        }
        return true;
    }

    @Override
    public void setValueAt(Object aValue, int row, int column) {
        if (aValue != null && column == 2) {
            String s = aValue.toString();
            if (!s.startsWith("Parcelable") && !s.startsWith("Serializable")) {
                setValueAt(Constant.PRIMITIVE_OR_STRING, row, 3);
            } else {
                Object value = getValueAt(row, 3);
                if (Constant.PRIMITIVE_OR_STRING.startsWith(value.toString())) {
                    setValueAt("", row, 3);
                }
            }
        }
        super.setValueAt(aValue, row, column);
    }


}
