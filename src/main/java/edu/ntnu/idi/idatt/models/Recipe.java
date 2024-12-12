package edu.ntnu.idi.idatt.models;

import java.util.List;

/**
 * Represent attributes that are included in a recipe such as name, description, instructions, list
 * of needed ingredients and servings.
 */
public class Recipe {

  private final String nameRecipe;
  private final String descriptionRecipe;
  private final String instructionsRecipe;
  private final List<Ingredient> ingredientsRecipe;
  private final int servingsRecipe;

  /**
   * Constructor that initializes a new recipe with the given attributes.
   *
   * @param nameRecipe         the name of the recipe
   * @param descriptionRecipe  a brief description of the recipe
   * @param instructionsRecipe a short instruction for the recipe
   * @param ingredients        a list of ingredients needed for the recipe
   * @param servingsRecipe     the number of servings the recipe makes
   * @throws IllegalArgumentException if the recipe name/description/instruction/list of ingredients
   *                                  is null or empty, or the servings is negative.
   */
  public Recipe(String nameRecipe, String descriptionRecipe, String instructionsRecipe,
      List<Ingredient> ingredients, int servingsRecipe) {
    if (nameRecipe == null || nameRecipe.isBlank()) {
      throw new IllegalArgumentException("Recipe name cannot be null or blank");
    }
    if (descriptionRecipe == null || descriptionRecipe.isBlank()) {
      throw new IllegalArgumentException("Recipe description cannot be null or blank");
    }
    if (instructionsRecipe == null || instructionsRecipe.isBlank()) {
      throw new IllegalArgumentException("Recipe instructions cannot be null or blank");
    }
    if (ingredients == null || ingredients.isEmpty()) {
      throw new IllegalArgumentException("Recipe ingredients cannot be null or empty");
    }
    if (servingsRecipe <= 0) {
      throw new IllegalArgumentException("Servings must be greater than zero");
    }

    this.nameRecipe = nameRecipe;
    this.instructionsRecipe = instructionsRecipe;
    this.descriptionRecipe = descriptionRecipe;
    // Sørger for at listen er immutable, kaster også NullPointException *ChatGPT
    this.ingredientsRecipe = List.copyOf(ingredients);
    this.servingsRecipe = servingsRecipe;
  }

  /**
   * Returns the name of the recipe.
   *
   * @return the recipe name
   */
  public String getNameRecipe() {
    return nameRecipe;
  }

  /**
   * Returns the description of the recipe.
   *
   * @return the recipe description
   */
  public String getDescriptionRecipe() {
    return descriptionRecipe;
  }

  /**
   * Returns the instruction for the recipe.
   *
   * @return the recipe instructions
   */
  public String getInstructionsRecipe() {
    return instructionsRecipe;
  }

  /**
   * Returns a immutable list of ingredients for the recipe.
   *
   * @return the list of ingredients for the recipe
   */
  public List<Ingredient> getIngredientsRecipe() {
    return ingredientsRecipe;
  }

  /**
   * Returns the number of servings the recipe makes.
   *
   * @return the number of servings
   */
  public int getServingsRecipe() {
    return servingsRecipe;
  }
}
