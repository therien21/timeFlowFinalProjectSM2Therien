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
public class Recipe implements Serializable {
    private String name;
    private String ingredients;
    private String prepTime;
    private String category;
    private String notes;
    
    public Recipe(String name, String ingredients, String prepTime, String category, String notes){
        this.name = name;
        this.ingredients = ingredients;
        this.prepTime = prepTime;
        this.category = category;
        this.notes = notes;
    }
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getIngredients(){
        return ingredients;
    }
    public void setIngredients(String ingredients){
        this.ingredients = ingredients;
    }
    public String getPrepTime(){
        return prepTime;
    }
    public void setPrepTime(String prepTime){
        this.prepTime = prepTime;
    }
    public String getCategory(){
        return category;
    }
    public void setCategory(String category){
        this.category = category;
    }
    public String getNotes(){
        return notes;
    }
    public void setNotes(String notes){
        this.notes = notes;
    }
    @Override
    public String toString(){
        return name;
    }
}
