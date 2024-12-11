package edu.ntnu.idi.idatt.models;

import java.util.List;

/**
 * This class represent attributes that are included in a recipe such as name, description,
 * instructions, list of needed ingredients and servings.
 */
public class Recipe {

  // Attributes
  private final String nameRecipe;
  private final String descriptionRecipe;
  private final String instructionsRecipe;
  private final List<Ingredient> ingredientsRecipe;
  private final int servingsRecipe;

  /**
   * Constructor that initializes a recipe with the given attributes.
   *
   * @throws IllegalArgumentException if the recipe name/description/instruction/list of ingredients
   *                                  is null or empty, or the servings is negative.
   */
  public Recipe(String nameRecipe, String descriptionRecipe, String instructionsRecipe,
      List<Ingredient> ingredients, int servingsRecipe) {
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

  /**
   * Gets the name of the recipe.
   *
   * @return the recipe name.
   */
  public String getNameRecipe() {
    return nameRecipe;
  }

  /**
   * Gets the description of the recipe.
   *
   * @return the recipe description
   */
  public String getDescriptionRecipe() {
    return descriptionRecipe;
  }

  /**
   * Gets the instruction for the recipe.
   *
   * @return the recipe instructions.
   */
  public String getInstructionsRecipe() {
    return instructionsRecipe;
  }

  /**
   * Gets the list of ingredients for the recipe.
   *
   * @return the list of ingredients for the recipe.
   */
  public List<Ingredient> getIngredientsRecipe() {
    return ingredientsRecipe;
  }

  /**
   * Gets how many servings for the recipe.
   *
   * @return how many servings for the recipe.
   */
  public int getServingsRecipe() {
    return servingsRecipe;
  }
}
