/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.timeflo.model;

/**
 *
 * @author theri
 */
public class Recipe {
    private String name;
    private String ingredients;
    
    public Recipe(String name, String ingredients){
        this.name = name;
        this.ingredients = ingredients;
    }
    public String getIngredients(){
        return ingredients;
    }
    public void setIngredients(String ingredients){
        this.ingredients = ingredients;
    }
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }
}
