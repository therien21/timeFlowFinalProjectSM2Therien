package com.mycompany.timeflo.model;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

public class ScheduleTableModel extends AbstractTableModel {
    private ArrayList<ScheduleItem> scheduleItems;
    private final String[] columns = {"Time", "Event", "Type", "Notes", ""};

    public ScheduleTableModel(ArrayList<ScheduleItem> scheduleItems) {
        this.scheduleItems = scheduleItems;
    }

    @Override
    public int getRowCount() {
        return scheduleItems.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public String getColumnName(int column) {
        return columns[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ScheduleItem item = scheduleItems.get(rowIndex);
        switch (columnIndex) {
            case 0: return item.getTime();
            case 1: return item.getEventName();
            case 2: return item.getType();
            case 3: return item.getNotes();
            case 4: return "AI ✨";
            default: return null;
        }
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return col == 4;
    }
}