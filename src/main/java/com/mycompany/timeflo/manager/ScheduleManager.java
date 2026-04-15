/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.timeflo.manager;

import com.mycompany.timeflo.model.ScheduleItem;
import java.util.ArrayList;
/**
 *
 * @author theri
 */
public class ScheduleManager {
    private ArrayList<ScheduleItem> scheduleItems;
    
    public ScheduleManager(){
        scheduleItems = new ArrayList<>();
    }  
    public void addScheduleItem(String time, String eventName, String type, String notes){
        scheduleItems.add(new ScheduleItem(time, eventName, type, notes));
    }
    public void addScheduleItem(ScheduleItem item){
        scheduleItems.add(item);
    }
    public void removeScheduleItem(ScheduleItem item){
    scheduleItems.remove(item);
    }
    public ArrayList<ScheduleItem> getScheduleItems(){
        return scheduleItems;
    }
    public boolean updateScheduleEvent(String time, String eventName){
        for(ScheduleItem item : scheduleItems){
            if (item.getTime().equals(time)){
                item.setEventName(eventName);
                return true;
            }
        }
        return false;
    }
}
