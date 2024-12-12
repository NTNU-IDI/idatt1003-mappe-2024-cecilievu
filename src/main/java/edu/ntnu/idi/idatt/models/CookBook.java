package edu.ntnu.idi.idatt.models;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents the "CookBook" part of the application that manages recipes. It allows users to store,
 * add, remove or expand a recipe.
 */
public class CookBook {

  /**
   * A list of recipes in the cookbook. This list stores all the recipes that have been added by the
   * user.
   */
  private final List<Recipe> recipes;

  /**
   * Constructor that initializes the cookbook with an empty list of recipes.
   */
  public CookBook() {
    this.recipes = new ArrayList<>();
  }

  /**
   * Returns a copy of all the recipes in the cookbook.
   *
   * @return a copy of the list of recipes in the cookbook.
   */
  public List<Recipe> getRecipes() {
    return new ArrayList<>(recipes);
  }

  /**
   * Expands and displays a specific recipe with its details which includes its instructions and
   * ingredients.
   *
   * @param recipeName the name of the recipe to expand.
   * @return a formatted string with full details of the recipe.
   * @throws IllegalArgumentException if the recipe is not found in the cookbook.
   */
  public String expandRecipe(String recipeName) {
    Recipe recipe = findRecipeByName(recipeName);
    // StringBuilder, endrer innhold uten å opprette nye objekter (mutable), *ChatGPT
    StringBuilder details = new StringBuilder();
    details.append("Recipe name: ").append(recipe.getNameRecipe()).append("\n")
        .append("Recipe description: ").append(recipe.getDescriptionRecipe()).append("\n")
        .append("Recipe instructions: ").append(recipe.getInstructionsRecipe()).append("\n")
        .append("Ingredients: \n");

    recipe.getIngredientsRecipe().forEach(ingredient ->
        details.append(String.format("- %s: %.2f %s",
            ingredient.getNameItem(),
            ingredient.getQuantityItem(),
            ingredient.getUnitItem()))
    );
    details.append("Servings: ").append(recipe.getServingsRecipe()).append("\n");
    return details.toString();
  }

  /**
   * Adds a new recipe to the cookbook. if the recipe is null or has an empty name, an empty list of
   * ingredients, or if the name is a duplicate, an exception will be thrown.
   *
   * @param newRecipe the new recipe to be added to the cookbook.
   * @return a message confirming that the recipe was added successfully.
   * @throws IllegalArgumentException if the recipe name is invalid or is a duplicate.
   */
  public String addRecipe(Recipe newRecipe) {
    // Sjekke at oppskriften ikke er null (NullPointerException)
    if (newRecipe == null) {
      throw new IllegalArgumentException("Recipe cannot be null");
    }
    if (newRecipe.getNameRecipe() == null || newRecipe.getNameRecipe().isBlank()) {
      throw new IllegalArgumentException("Recipe name cannot be null or blank");
    }
    if (newRecipe.getIngredientsRecipe() == null || newRecipe.getIngredientsRecipe().isEmpty()) {
      throw new IllegalArgumentException("A recipe must at least have one ingredient");
    }
    if (recipes.stream()
        .anyMatch(r -> r.getNameRecipe().equalsIgnoreCase(newRecipe.getNameRecipe()))) {
      throw new IllegalArgumentException(
          String.format("A recipe with the name '%s' already exist in the cookbook"
              + ". Try with a different name.", newRecipe.getNameRecipe()));
    }

    recipes.add(newRecipe);
    return String.format("The recipe '%s' is added to the cookbook.", newRecipe.getNameRecipe());
  }

  /**
   * Removes a specific recipe from the cookbook.
   *
   * @param recipeName the name of recipe to be removed.
   * @return a message confirming that the recipe is removed successfully.
   * @throws IllegalArgumentException if the recipe does not exist in the cookbook.
   */
  public String removeRecipe(String recipeName) {
    Recipe recipeToRemove = findRecipeByName(recipeName);
    recipes.remove(recipeToRemove);
    return String.format("The recipe '%s' is removed from the cookbook.", recipeName);
  }

  /**
   * Checking if a recipe kan be made by items/ingredients in the "fridge".
   *
   * @param recipeName  the name of the recipe to check
   * @param foodStorage the "fridge" to check for available ingredients
   * @return a message indicating whether the recipe can be made, or which ingredients are missing.
   * @throws IllegalArgumentException if the recipe does not exist in the cookbook.
   */
  public String canMakeRecipe(String recipeName, FoodStorage foodStorage) {
    Recipe recipe = findRecipeByName(recipeName);
    StringBuilder result = new StringBuilder();
    boolean canMake = recipe.getIngredientsRecipe().stream()
        .allMatch(ingredient -> isIngredientAvailable(ingredient, foodStorage, result));

    if (canMake) {
      return "You have all the ingredients to make " + recipeName + "!\n";
    }
    result.insert(0, "You do not have all the ingredients to make " + recipeName + "\n");
    return result.toString();
  }

  /**
   * Returns a list of suggested recipes based by items/ingredients in the "fridge".
   *
   * @param foodStorage the "fridge" to check for available ingredients
   * @return a list of recipe names that can be made
   */
  public List<String> suggestRecipe(FoodStorage foodStorage) {
    return recipes.stream()
        .filter(recipe -> canMake(recipe, foodStorage))
        .map(Recipe::getNameRecipe)
        .collect(Collectors.toList());
  }

  // Ekstra metoder som kan bli gjenbrukt for å finne en oppskrift etter navn eller sjekke om det er
  // nok varer i kjøleskap for å lage en oppskrift.
  // Hjelp fra *ChatGPT

  /**
   * Finds a recipe by name (case-insensitive) in the cookbook.
   *
   * @param recipeName the name of the recipe to find
   * @return the matching recipe
   * @throws IllegalArgumentException if the recipe does not exist in the cookbook
   */
  private Recipe findRecipeByName(String recipeName) {
    return recipes.stream()
        .filter(recipe -> recipe.getNameRecipe().equalsIgnoreCase(recipeName))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException(
            (String.format("The recipe '%s' does not exist in the cookbook.", recipeName))
        ));
  }

  /**
   * Checking if a specific ingredient is available in the fridge.
   *
   * @param ingredient  the ingredient to check
   * @param foodStorage the fridge to check for the ingredient
   * @param result      the result to update if the ingredient is not available
   * @return true if the ingredient is available, false otherwise
   */
  private boolean isIngredientAvailable(Ingredient ingredient, FoodStorage foodStorage,
      StringBuilder result) {
    // Sjekker om alle ingredienser er i kjøleskapet
    Ingredient available = foodStorage.getItems().stream()
        .filter(item -> item.getNameItem().equalsIgnoreCase(ingredient.getNameItem()))
        .findFirst()
        .orElse(null);

    // Hvis ingrediensene ikke finnes eller det er for lite av den
    if (available == null || available.getQuantityItem() < ingredient.getQuantityItem()) {
      double missingAmount =
          ingredient.getQuantityItem() - (available == null ? 0 : available.getQuantityItem());
      result.append(String.format("Missing: %s (you need %.2f %s)",
          ingredient.getNameItem(), missingAmount, ingredient.getUnitItem()));
      return false;
    }
    return true;
  }

  /**
   * Checks if a recipe can be made with the ingredients in the fridge.
   *
   * @param recipe      the recipe to check
   * @param foodStorage the fridge to check for available ingredients
   * @return true if the recipe can be made, false otherwise
   */
  private boolean canMake(Recipe recipe, FoodStorage foodStorage) {
    return recipe.getIngredientsRecipe().stream()
        .allMatch(ingredient ->
            foodStorage.getItems().stream()
                .anyMatch(item -> item.getNameItem().equalsIgnoreCase(ingredient.getNameItem())
                    && item.getQuantityItem() >= ingredient.getQuantityItem())
        );
  }
}
