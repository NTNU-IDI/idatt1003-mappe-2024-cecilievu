package edu.ntnu.idi.idatt.models;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

public class TestCookBook {

  CookBook cookbook;
  Recipe recipe;

  @BeforeEach
  public void setUp() {
    cookbook = new CookBook();
    List<Ingredient> ingredients = new ArrayList<>();
    ingredients.add(new Ingredient("Egg", 12, "pcs", 2.0, LocalDate.of(2024, 12, 24)));
    ingredients.add(new Ingredient("Milk", 3, "dL", 10.0, LocalDate.of(2024, 12, 20)));
    ingredients.add(new Ingredient("Butter", 250, "grams", 0.1, LocalDate.of(2024, 12, 10)));

    recipe = new Recipe("Cake", "Dessert", "Mix and bake", ingredients, 6);
  }

  @Nested
  @DisplayName("Negative tests for CookBook")
  class NegativeTests {

    @Test
    @DisplayName("expandRecipe() throws IllegalArgumentException if the recipe does not exist")
    public void testExpandRecipe_throwsException_ifRecipeDoesNotExist() {
      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
        cookbook.expandRecipe("NonExistent");
      });
      assertEquals("The recipe 'NonExistent' does not exist in the cookbook.",
          exception.getMessage());
    }

    @Test
    @DisplayName("addRecipe() throws IllegalArgumentException if recipe is null")
    public void testAddRecipe_throwsException_ifRecipeIsNull() {
      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
        cookbook.addRecipe(null);
      });
      assertEquals("Recipe cannot be null", exception.getMessage());
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", " "})
    @DisplayName("addRecipe() throws IllegalArgumentException on null or blank recipe name")
    public void testAddRecipe_throwsException_ifRecipeNameIsNullOrBlank(String invalidName) {
      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
        new CookBook().addRecipe(
            new Recipe(invalidName, "Dessert", "Mix and bake", recipe.getIngredientsRecipe(), 4));
      });
      assertEquals("Recipe name cannot be null or blank", exception.getMessage());
    }


    @Test
    @DisplayName("addRecipe() throws IllegalArgumentException if the recipe name is a duplicate")
    public void testAddRecipe_throwsException_ifNameIsDuplicate() {
      cookbook.addRecipe(recipe);
      Recipe duplicateRecipe = new Recipe("Cake", "Dessert", "Mix and bake",
          recipe.getIngredientsRecipe(), 4);
      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
        cookbook.addRecipe(duplicateRecipe);
      });
      assertEquals(
          "A recipe with the name 'Cake' already exist in the cookbook. Try with a different name.",
          exception.getMessage());
    }

    @Test
    @DisplayName("removeRecipe() throws IllegalArgumentException if the recipe does not exist")
    public void testRemoveRecipe_returnsError_ifRecipeNotFound() {
      CookBook cookbook = new CookBook();
      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
        cookbook.removeRecipe("NonExistent");
      });
      assertEquals("The recipe 'NonExistent' does not exist in the cookbook.",
          exception.getMessage());
    }
  }

  @Nested
  @DisplayName("Positive tests for CookBook")
  class PositiveTests {

    @Test
    @DisplayName("expandRecipe() returns full details of a recipe successfully")
    public void testExpandRecipe_returnFullDetails() {
      cookbook.addRecipe(recipe);
      String details = cookbook.expandRecipe("Cake");
      System.out.println(details);
      assertTrue(details.contains("Recipe name: Cake"));
      assertTrue(details.contains("Recipe description: Dessert"));
      assertTrue(details.contains("Recipe instructions: Mix and bake"));
      assertTrue(details.contains("Ingredients: "));
      assertTrue(details.contains("- Egg: 12.00 pcs"));
      assertTrue(details.contains("- Milk: 3.00 dL"));
      assertTrue(details.contains("- Butter: 250.00 grams"));
      assertTrue(details.contains("Servings: 6"));
    }

    @Test
    @DisplayName("addRecipe() adding a recipe successfully")
    public void testAddRecipe_addsRecipeSuccessfully() {
      String result = cookbook.addRecipe(recipe);
      assertEquals("The recipe 'Cake' is added to the cookbook.", result);
      assertEquals(1, cookbook.getRecipes().size());
    }

    @Test
    @DisplayName("removeRecipe() removes a recipe successfully")
    public void testRemoveRecipe_removesRecipeSuccessfully() {
      cookbook.addRecipe(recipe);
      String result = cookbook.removeRecipe("Cake");
      assertEquals("The recipe 'Cake' is removed from the cookbook.", result);
      assertTrue(cookbook.getRecipes().isEmpty());
    }
  }
}