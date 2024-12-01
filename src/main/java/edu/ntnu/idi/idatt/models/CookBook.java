package edu.ntnu.idi.idatt.models;

import java.util.ArrayList;
import java.util.List;

public class CookBook {

    private List<Recipe> recipes;

    public CookBook() {
        this.recipes = new ArrayList<>();
    }

    public String addRecipe(Recipe newRecipe) {
        if (recipes.stream().anyMatch(r -> r.getNameRecipe().equals(newRecipe.getNameRecipe()))) {
            return String.format("A recipe with the name '%s' already exist in the cookbook.", newRecipe.getNameRecipe());
        }
        recipes.add(newRecipe);
        return String.format("The recipe '%s' is added to the cookbook.", newRecipe.getNameRecipe());
    }

    public String removeRecipe(String recipeName) {
        Recipe recipeToRemove = recipes.stream()
                .filter(recipe -> recipe.getNameRecipe().equalsIgnoreCase(recipeName))
                .findFirst()
                .orElse(null);

        if (recipeToRemove != null) {
            recipes.remove(recipeToRemove);
            return String.format("The recipe '%s' is removed from the cookbook.", recipeName);
        } else {
            return String.format("The recipe '%s' does not exist in the cookbook.", recipeName);
        }
    }

    public List<Recipe> getRecipes() {
        return new ArrayList<>(recipes);
    }

    public String expandRecipe(String recipeName) {
        return recipes.stream()
                .filter(recipe -> recipe.getNameRecipe().equalsIgnoreCase(recipeName))
                .findFirst()
                .map(recipe -> {
                    StringBuilder details = new StringBuilder(); // En klasse endrer innhold uten å opprette nye objekter (mutable)
                    details.append("Recipe name: ").append(recipe.getNameRecipe()).append("\n");
                    details.append("Recipe description: ").append(recipe.getDescriptionRecipe()).append("\n");
                    details.append("Recipe instructions: ").append(recipe.getInstructionsRecipe()).append("\n");
                    details.append("Ingredients: \n");
                    recipe.getIngredientsRecipe().forEach(ingredient ->
                        details.append(String.format("- %s: %.2f %s\n",
                                ingredient.getNameItem(), ingredient.getQuantityItem(), ingredient.getUnitItem()))
                    );
                    details.append("Servings: ").append(recipe.getServingsRecipe()).append("\n");
                    return details.toString();
                })
                .orElse("Recipe not found in cookbook");
    }

    public boolean canMakeRecipe(Recipe recipe, List<Ingredient> fridgeItems) {
        return recipe.getIngredientsRecipe().stream().allMatch(recipeIngredient ->
                fridgeItems.stream().anyMatch(fridgeIngredient -> fridgeIngredient.getNameItem().equals(recipeIngredient.getNameItem()) &&
                        fridgeIngredient.getQuantityItem() >= recipeIngredient.getQuantityItem()
                )
        );
    }

    public void suggestRecipe() {

    }
}
