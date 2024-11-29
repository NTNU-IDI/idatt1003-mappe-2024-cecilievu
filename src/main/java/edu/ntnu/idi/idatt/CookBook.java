package edu.ntnu.idi.idatt;

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

    public void removeRecipe(Recipe recipeToRemove) {
        recipes.remove(recipeToRemove);
    }

    public List<Recipe> getRecipes() {
        return new ArrayList<>(recipes);
    }

    public List<Recipe> searchRecipe(String recipeName) {
        return recipes.stream()
                .filter(recipe -> recipe.getNameRecipe().equals(recipeName))
                .toList();
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
