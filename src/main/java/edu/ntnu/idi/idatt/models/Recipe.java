package edu.ntnu.idi.idatt.models;

import java.util.List;

public class Recipe {
    //attributes
    private final String nameRecipe;
    private final String descriptionRecipe;
    private final String instructionsRecipe;
    private final List<Ingredient> ingredientsRecipe;
    private final int servingsRecipe;

    public Recipe(String nameRecipe, String descriptionRecipe, String instructionsRecipe, List<Ingredient> ingredients, int servingsRecipe) {
        if (nameRecipe == null || nameRecipe.isEmpty()) {
            throw new IllegalArgumentException("Recipe name cannot be null or empty");
        }
        if (descriptionRecipe == null || descriptionRecipe.isEmpty()) {
            throw new IllegalArgumentException("Recipe description cannot be null or empty");
        }
        if (instructionsRecipe == null || instructionsRecipe.isEmpty()) {
            throw new IllegalArgumentException("Recipe instructions cannot be null or empty");
        }
        if (ingredients == null || ingredients.isEmpty()) {
            throw new IllegalArgumentException("Recipe ingredients cannot be null or empty");
        }
        if (servingsRecipe <= 0) {
            throw new IllegalArgumentException("Servings recipe must be greater than zero");
        }
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
