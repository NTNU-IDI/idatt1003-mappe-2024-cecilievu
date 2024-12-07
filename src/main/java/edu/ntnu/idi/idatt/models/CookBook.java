package edu.ntnu.idi.idatt.models;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CookBook {

    private List<Recipe> recipes;

    public CookBook() {
        this.recipes = new ArrayList<>();
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

    public String canMakeRecipe(String recipeName, FoodStorage foodStorage) {
        // Finner oppskrift basert på navn
        Recipe recipe = recipes.stream()
                .filter(r -> r.getNameRecipe().equalsIgnoreCase(recipeName))
                .findFirst()
                .orElse(null);

        if (recipe == null) {
            return "Recipe not found in cookbook"; // Avslutter hvis oppskriften ikke finnes
        }

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
                double missingAmount = ingredient.getQuantityItem() - (available == null ? 0 : available.getQuantityItem());
                result.append(String.format("Missing: %s (you need %.2f %s)\n", ingredient.getNameItem(), missingAmount, ingredient.getUnitItem()));
                canMake = false;
            }
        }

        if (canMake) {
            return "You have all the ingredients to make " + recipeName + "!\n";
        }

        result.insert(0, "You do not have all the ingredients to make " + recipeName + "\n");
        return result.toString();
    }

    public List<String> suggestRecipe(FoodStorage foodStorage) {
        return recipes.stream()
                .filter(recipe -> recipe.getIngredientsRecipe().stream()
                        .allMatch(ingredient -> {
                            Ingredient available = foodStorage.getItems().stream()
                                    .filter(item -> item.getNameItem().equalsIgnoreCase(ingredient.getNameItem()))
                                    .findFirst()
                                    .orElse(null);
                            return available != null && available.getQuantityItem() >= ingredient.getQuantityItem();
                        })
                )
                .map(Recipe::getNameRecipe)
                .collect(Collectors.toList());
    }
}
