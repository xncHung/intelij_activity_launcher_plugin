package cn.xjp.plugins.android_act_launcher.rule;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

class ParamItemModel extends DefaultTableModel {

    public ParamItemModel() {
        super(IntentParam.COLUMN_NAMES,0);
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex==2)return Array.class;
        if (getValueAt(0,columnIndex)==null)return Object.class;
        return getValueAt(0,columnIndex).getClass();
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }
}
