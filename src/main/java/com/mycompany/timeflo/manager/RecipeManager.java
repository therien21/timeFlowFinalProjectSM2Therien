/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.timeflo.manager;

import com.mycompany.timeflo.model.Recipe;
import java.util.ArrayList;
/**
 *
 * @author theri
 */
public class RecipeManager {
    private ArrayList<Recipe> recipes;
    
    public RecipeManager(){
        recipes = new ArrayList<>();
    }
    public void addRecipe(String name, String ingredients){
        recipes.add(new Recipe(name, ingredients));
    }
    public void addRecipe(Recipe recipe){
        recipes.add(recipe);
    }
    public ArrayList<Recipe> getRecipes(){
        return recipes;
    }
    
}
