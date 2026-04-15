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
    public void addRecipe(String name, String ingredients, String prepTime, String category, String notes){
        recipes.add(new Recipe(name, ingredients, prepTime, category, notes));
    }
    public void addRecipe(Recipe recipe){
        recipes.add(recipe);
    }
    public void removeRecipe(Recipe recipe){
        recipes.remove(recipe);
    }
    public ArrayList<Recipe> getRecipes(){
        return recipes;
    }
    public void setRecipes(ArrayList<Recipe> recipes) {
        this.recipes = recipes;
    }
}
