package edu.ntnu.idi.idatt.models;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class represents the "CookBook" part of the application that manages recipes. It allows the
 * user among other things such as store, add or expand a recipe.
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
   * Returns a list of all recipes in the cookbook. This method creates a copy of the recipe list to
   * avoid external changes from affecting the original list in the cookbook.
   *
   * @return a copy of the list of recipes in the cookbook.
   */
  public List<Recipe> getRecipes() {
    return new ArrayList<>(recipes);
  }

  /**
   * Expands and displays a specific recipe with its details which includes its instructions and
   * ingredients. This method searches for the specific recipe in the cookbook by using its name
   * (case-insensitive).
   *
   * @param recipeName the name of the recipe to expand.
   * @return a formatted string with full details of the recipe, including name, description,
   * instruction, ingredients and servings.
   * @throws IllegalArgumentException if the recipe is not found in the cookbook.
   */
  public String expandRecipe(String recipeName) {
    return recipes.stream()
        .filter(recipe -> recipe.getNameRecipe().equalsIgnoreCase(recipeName))
        .findFirst()
        .map(recipe -> {
          // StringBuilder, endrer innhold uten å opprette nye objekter (mutable)
          StringBuilder details = new StringBuilder();
          details.append("Recipe name: ").append(recipe.getNameRecipe()).append("\n");
          details.append("Recipe description: ").append(recipe.getDescriptionRecipe()).append("\n");
          details.append("Recipe instructions: ").append(recipe.getInstructionsRecipe())
              .append("\n");
          details.append("Ingredients: \n");
          recipe.getIngredientsRecipe()
              .forEach(ingredient -> details.append(String.format("- %s: %.2f %s",
                  ingredient.getNameItem(), ingredient.getQuantityItem(),
                  ingredient.getUnitItem())));
          details.append("Servings: ").append(recipe.getServingsRecipe()).append("\n");
          return details.toString();
        })
        .orElseThrow(() -> new IllegalArgumentException("Recipe not found in cookbook"));
  }

  /**
   * Adds a new recipe to the cookbook. This method checks if the recipe is valid before it's added
   * to the cookbook by verifying if the recipe name is a duplicate of an existing recipe name, if
   * the recipe has an empty name, or an empty list of ingredients. If this happens, an exception
   * will be thrown.
   *
   * @param newRecipe the new recipe to be added to the cookbook.
   * @return a message indicating that the recipe was added successfully.
   * @throws IllegalArgumentException if the recipe is null, if its name is null or empty, if the
   *                                  recipe has no ingredients, or if the name is a duplicate of an
   *                                  existing recipe in the cookbook.
   */
  public String addRecipe(Recipe newRecipe) {
    // Sjekke at oppskriften ikke er null (NullPointerException)
    if (newRecipe == null) {
      throw new IllegalArgumentException("Recipe cannot be null");
    }
    if (newRecipe.getNameRecipe() == null || newRecipe.getNameRecipe().isEmpty()) {
      throw new IllegalArgumentException("Recipe name cannot be null or empty");
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
   * Removes a specific recipe from the cookbook. This method searches for the recipe by using its
   * name (case-insensitive). If the recipe is found, it will be removed from the internal list. If
   * the recipe is not found, an exception will be thrown.
   *
   * @param recipeName the recipe to be removed.
   * @return a message that indicate that the recipe is removed successfully.
   * @throws IllegalArgumentException if the recipe does not exist in the cookbook.
   */
  public String removeRecipe(String recipeName) {
    return recipes.stream()
        .filter(recipe -> recipe.getNameRecipe().equalsIgnoreCase(recipeName))
        .findFirst()
        .map(recipeToRemove -> {
          recipes.remove(recipeToRemove);
          return String.format("The recipe '%s' is removed from the cookbook.", recipeName);
        })
        .orElseThrow(() -> new IllegalArgumentException(
            (String.format("The recipe '%s' does not exist in the cookbook.", recipeName))));
  }

  /**
   * Checking if a recipe kan be made by items/ingredients in the "fridge". This method searches for
   * a recipe by name (case-insensitive) and then iterating through the available ingredients in
   * store. If any ingredients are missing, it will return a message with the missing ingredient and
   * quantity. If the recipe does not exist, an exception will be thrown.
   *
   * @param recipeName  the name of the recipe to check
   * @param foodStorage the "fridge" to check for available ingredients
   * @return a message that indicates whether the recipe can be made, or which ingredients are
   * missing
   * @throws IllegalArgumentException if the recipe does not exist in the cookbook.
   */
  public String canMakeRecipe(String recipeName, FoodStorage foodStorage) {
    // Finner oppskrift basert på navn
    Recipe recipe = recipes.stream()
        .filter(r -> r.getNameRecipe().equalsIgnoreCase(recipeName))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException(
            String.format("The recipe '%s' does not exist in the cookbook.", recipeName)));

    StringBuilder result = new StringBuilder();
    boolean canMake = true;

    // Sjekker om alle ingredienser er i kjøleskapet
    for (Ingredient ingredient : recipe.getIngredientsRecipe()) {
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
        canMake = false;
      }
    }
    if (canMake) {
      return "You have all the ingredients to make " + recipeName + "!\n";
    }
    result.insert(0, "You do not have all the ingredients to make " + recipeName + "\n");
    return result.toString();
  }

  /**
   * Returns a list of suggested recipes based by items/ingredients in the "fridge". This method
   * iterates through all the recipes in the cookbook and then checks if all the needed ingredients
   * and quantities are in store. If a recipe can be made, it's added to the list of suggestions.
   *
   * @param foodStorage the "fridge" to check for available ingredients.
   * @return a list of recipe names that can be made with items in the fridge.
   */
  public List<String> suggestRecipe(FoodStorage foodStorage) {
    return recipes.stream()
        .filter(recipe -> recipe.getIngredientsRecipe()
            .stream()
            .allMatch(ingredient -> {
              Ingredient available = foodStorage.getItems().stream()
                  .filter(item -> item.getNameItem().equalsIgnoreCase(ingredient.getNameItem()))
                  .findFirst()
                  .orElse(null);
              return available != null
                  && available.getQuantityItem() >= ingredient.getQuantityItem();
            })
        )
        .map(Recipe::getNameRecipe)
        .collect(Collectors.toList());
  }
}
