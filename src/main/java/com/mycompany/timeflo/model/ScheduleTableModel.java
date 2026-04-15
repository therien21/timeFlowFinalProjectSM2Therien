/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.timeflo.model;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;
/**
 *
 * @author theri
 */
public class ScheduleTableModel extends AbstractTableModel{
    private ArrayList<ScheduleItem> scheduleItems;
    private final String[] columns = {"Time", "Event", "Type", "Notes"};
    
    public ScheduleTableModel(ArrayList<ScheduleItem> scheduleItems){
        this.scheduleItems = scheduleItems;
    }
    @Override
    public int getRowCount(){
        return scheduleItems.size();
    }
    @Override
    public int getColumnCount(){
        return columns.length;
    }
    @Override
    public String getColumnName(int column){
        return columns[column];
    }
    @Override
    public Object getValueAt(int rowIndex, int columnIndex){
        ScheduleItem item = scheduleItems.get(rowIndex);
        if (columnIndex == 0){
            return item.getTime();
        } else if (columnIndex == 1){
            return item.getEventName();
        } else if (columnIndex == 2){
            return item.getType();
        } else if (columnIndex == 3){
            return item.getNotes();
        } else {
            return null;
        }
        
    }
}
