package edu.ntnu.idi.idatt;

import java.util.ArrayList;
import java.util.List;

public class Recipe {
    //attributes
    private final String nameRecipe;
    private final String descriptionRecipe;
    private final String instructionsRecipe;
    private List<Ingredient> ingredientsRecipe;
    private int servingsRecipe;

    public Recipe(String nameRecipe, String descriptionRecipe, String instructionsRecipe, List<Ingredient> ingredients, int servingsRecipe) {
        this.nameRecipe = nameRecipe;
        this.instructionsRecipe = instructionsRecipe;
        this.descriptionRecipe = descriptionRecipe;
        this.ingredientsRecipe = ingredients;
        this.servingsRecipe = servingsRecipe;
    }

    public String getNameRecipe() {
        return nameRecipe;
    }

    public String getDescriptionRecipe() {
        return descriptionRecipe;
    }

    public String getInstructionsRecipe() {
        return instructionsRecipe;
    }

    public List<Ingredient> getIngredientsRecipe() {
        return ingredientsRecipe;
    }

    public int getServingsRecipe() {
        return servingsRecipe;
    }
}
