/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.timeflo.model;

/**
 *
 * @author theri
 */
public class Task {
   private String title;
   private boolean completed;
   
   public Task(String title){
       this.title = title;
       this.completed = false;
   }
   public String getTitle(){
       return title;
   }
   public void setTitle(String title){
       this.title = title;
   }
   public boolean isCompleted(){
       return completed;
   }
   public void markComplete(){
       this.completed = true;
   }
   public void markIncomplete(){
       this.completed = false;
   }
   @Override
   public String toString(){
       if(completed){
           return "[Done] " + title;
       }
       return title;
   }
}
