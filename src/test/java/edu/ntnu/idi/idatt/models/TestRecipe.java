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
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

public class TestRecipe {

  Recipe recipe;

  /**
   * Sets up a recipe with standard test values. This method runs before each test.
   */
  @BeforeEach
  public void setUp() {
    List<Ingredient> ingredients = new ArrayList<>();
    ingredients.add(new Ingredient("Egg", 2, "pcs", 0.0, LocalDate.MAX));
    ingredients.add(new Ingredient("Milk", 0.25, "L", 0.0, LocalDate.MAX));
    ingredients.add(new Ingredient("Butter", 50, "grams", 0.0, LocalDate.MAX));
    ingredients.add(new Ingredient("Flour", 150, "grams", 0.0, LocalDate.MAX));
    ingredients.add(new Ingredient("Baking soda", 6, "grams", 0.0, LocalDate.MAX));
    ingredients.add(new Ingredient("Sugar", 9, "grams", 0.0, LocalDate.MAX));

    recipe = new Recipe("Pannekake", "Breakfast dish", "Pour batter in pan", ingredients, 2);
  }

  /**
   * Negative tests
   */
  @Nested
  @DisplayName("Negative tests for Recipe, throws exceptions on wrong input")
  public class methodsThrowsExceptions {

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", " "})
    @DisplayName("Constructor throws IllegalArgumentException on null or blank name")
    public void testConstructor_throwsException_onNullOrBlankName(String invalidName) {
      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
        List<Ingredient> ingredients = List.of(new Ingredient("Egg", 2, "pcs", 0.0, LocalDate.MAX));
        new Recipe(invalidName, "Breakfast dish", "Pour batter in pan", ingredients, 2);
      });
      assertEquals("Recipe name cannot be null or blank", exception.getMessage());
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", " "})
    @DisplayName("Constructor throws IllegalArgumentException on null or blank description")
    public void testConstructor_throwsException_onNullOrBlankDescription(
        String invalidDescription) {
      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
        List<Ingredient> ingredients = List.of(new Ingredient("Egg", 2, "pcs", 0.0, LocalDate.MAX));
        new Recipe("Pannekake", invalidDescription, "Pour batter in pan", ingredients, 2);
      });
      assertEquals("Recipe description cannot be null or blank", exception.getMessage());
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", " "})
    @DisplayName("Constructor throws IllegalArgumentException on null or blank instruction")
    public void testConstructor_throwsException_onNullOrBlankInstruction(
        String invalidInstruction) {
      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
        List<Ingredient> ingredients = List.of(new Ingredient("Egg", 2, "pcs", 0.0, LocalDate.MAX));
        new Recipe("Pannekake", "Breakfast dish", invalidInstruction, ingredients, 2);
      });
      assertEquals("Recipe instructions cannot be null or blank", exception.getMessage());
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    @DisplayName("Constructor throws IllegalArgumentException on null or empty list of ingredients")
    public void testConstructor_throwsException_onNullOrEmptyIngredients(
        List<Ingredient> invalidIngredients) {
      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
          new Recipe("Pannekake", "Breakfast dish", "Pour batter in pan", invalidIngredients, 2));
      assertEquals("Recipe ingredients cannot be null or empty", exception.getMessage());
    }


    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    @DisplayName("Constructor throws IllegalArgumentException on negative or zero servings")
    public void testConstructor_throwsException_onNegativeOrZeroServings(int invalidServings) {
      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
        List<Ingredient> ingredients = List.of(new Ingredient("Egg", 2, "pcs", 0.0, LocalDate.MAX));
        new Recipe("Pannekake", "Breakfast dish", "Pour batter in pan", ingredients,
            invalidServings);
      });
      assertEquals("Servings must be greater than zero", exception.getMessage());
    }
  }

  @Nested
  @DisplayName("Positive tests for Recipe with valid input")
  public class methodsDoesNotThrowExceptions {

    @Test
    @DisplayName("Constructor successfully creates a recipe with valid input")
    public void testConstructor_createsRecipeWithValidInput() {
      List<Ingredient> ingredients = List.of(
          new Ingredient("Egg", 2, "pcs", 0.0, LocalDate.MAX),
          new Ingredient("Milk", 0.25, "L", 0.0, LocalDate.MAX));
      Recipe recipe = new Recipe("Pannekake", "Breakfast dish", "Pour batter in pan", ingredients,
          2);
      assertEquals("Pannekake", recipe.getNameRecipe());
      assertEquals("Breakfast dish", recipe.getDescriptionRecipe());
      assertEquals("Pour batter in pan", recipe.getInstructionsRecipe());
      assertEquals(ingredients, recipe.getIngredientsRecipe());
      assertEquals(2, recipe.getIngredientsRecipe().size());
    }

    @Test
    @DisplayName("Constructor creates a recipe with minimum valid servings (1)")
    public void testConstructor_createsRecipeWithMinimumValidServings() {
      List<Ingredient> ingredients = List.of(new Ingredient("Egg", 2, "pcs", 0.0, LocalDate.MAX));
      Recipe recipe = new Recipe("Pannekake", "Breakfast dish", "Pour batter in pan", ingredients,
          1);
      assertEquals(1, recipe.getServingsRecipe());
    }
  }
}



