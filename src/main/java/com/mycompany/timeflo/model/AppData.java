/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.timeflo.model;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author theri
 */
public class AppData implements Serializable{
    private ArrayList<Task> tasks;
    private ArrayList<ScheduleItem> scheduleItems;
    private ArrayList<Recipe> recipes;
    
    public AppData(ArrayList<Task> tasks,ArrayList<ScheduleItem> scheduleItems, ArrayList<Recipe> recipes){
        this.tasks = tasks;
        this.scheduleItems = scheduleItems;
        this.recipes = recipes;
    }
    public ArrayList<Task> getTasks(){
        return tasks;
    }
    public ArrayList<ScheduleItem> getScheduleItems() {
        return scheduleItems;
    }
    public ArrayList<Recipe> getRecipes(){
        return recipes;
    }
    
}
