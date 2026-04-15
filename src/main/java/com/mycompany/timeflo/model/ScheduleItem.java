/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.timeflo.model;
import java.io.Serializable;

/**
 *
 * @author theri
 */
public class ScheduleItem implements Serializable {
    private String time;
    private String eventName;
    private String type;
    private String notes;
    
    public ScheduleItem(String time, String eventName, String type, String notes){
        this.time = time;
        this.eventName = eventName;
        this.type = type;
        this.notes = notes;
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
    public String getType(){
        return type;
    }
    public void setType(String type){
        this.type = type;
    }
    public String getNotes(){
        return notes;
    }
    public void setNotes(String notes){
        this.notes = notes;
    }
    @Override
    public String toString(){
        return time + " - " + eventName;
    }
    
}
