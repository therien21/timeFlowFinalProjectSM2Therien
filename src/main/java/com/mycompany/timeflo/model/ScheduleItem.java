/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.timeflo.model;

/**
 *
 * @author theri
 */
public class ScheduleItem {
    private String time;
    private String eventName;
    
    public ScheduleItem(String time, String eventName){
        this.time = time;
        this.eventName = eventName;
    }
    public String getTime(){
        return time;
    }
    public void setTime(String time){
        this.time = time;
    }
    public String getEventName(){
        return eventName;
    }
    public void setEventName(String eventName){
        this.eventName = eventName;
    }
    @Override
    public String toString(){
        return time + " - " + eventName;
    }
    
}
