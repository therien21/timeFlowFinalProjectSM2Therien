/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.timeflo.model;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;
/**
 *
 * @author theri
 */
public class RecipeTableModel extends AbstractTableModel {
    private ArrayList<Recipe> recipes;
    private final String[] columns = {
        "Name", "Ingredients", "Prep Time", "Category", "Notes"
    };
    public RecipeTableModel(ArrayList<Recipe> recipes){
        this.recipes = recipes;
    }
    @Override
    public int getRowCount(){
        return recipes.size();
    }
    @Override
    public int getColumnCount(){
        return columns.length;
    }
    @Override
    public String getColumnName(int column){
        return columns[column];
    }
    @Override
    public Object getValueAt(int rowIndex, int columnIndex){
        Recipe recipe = recipes.get(rowIndex);
        if (columnIndex == 0){
            return recipe.getName();
        } else if (columnIndex == 1){
            return recipe.getIngredients();
        } else if (columnIndex == 2){
            return recipe.getPrepTime();
        } else if (columnIndex == 3){
            return recipe.getCategory();
        } else if (columnIndex == 4){
            return recipe.getNotes();
        } else{
            return null;
        }
    }
}
