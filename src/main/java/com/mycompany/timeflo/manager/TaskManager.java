/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.timeflo.manager;
import com.mycompany.timeflo.model.Task;
import java.util.ArrayList;
/**
 *
 * @author theri
 */
public class TaskManager {
    private ArrayList<Task> tasks;
    public TaskManager(){
        tasks = new ArrayList<>();
    }
    public void addTask(String title){
        tasks.add(new Task(title));
    }
    public void addTask(Task task){
        tasks.add(task);
    }
    public void removeTask(Task task){
        tasks.remove(task);
    }
    public ArrayList<Task> getTasks(){
        return tasks;
    }
    public void markTaskCompleted(Task task){
        task.markComplete();
    }
}
