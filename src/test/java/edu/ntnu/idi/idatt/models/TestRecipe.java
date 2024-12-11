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

class TestRecipe {

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

  // nested lar meg grupperere relaterte tester
  @Nested
  @DisplayName("Negative tests for Recipe, throws exceptions on wrong invalid input")
  public class NegativeTests {

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", " "})
    @DisplayName("Constructor throws IllegalArgumentException on null name")
    public void testConstructor_throwsException_onNullName(String invalidName) {
      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
        List<Ingredient> ingredients = List.of(new Ingredient("Egg", 2, "pcs", 0.0, LocalDate.MAX));
        new Recipe(invalidName, "Breakfast dish", "Pour batter in pan", ingredients, 2);
      });
      assertEquals("Recipe name cannot be null or blank", exception.getMessage());
    }

    @Test
    @DisplayName("Constructor throws IllegalArgumentException on blank name")
    public void testConstructor_throwsException_onBlankName() {
      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
        List<Ingredient> ingredients = List.of(new Ingredient("Egg", 2, "pcs", 0.0, LocalDate.MAX));
        new Recipe(" ", "Breakfast dish", "Pour batter in pan", ingredients, 2);
      });
      assertEquals("Recipe name cannot be null or blank", exception.getMessage());
    }

    @Test
    @DisplayName("Constructor throws IllegalArgumentException on null description")
    public void testConstructor_throwsException_onNullDescription() {
      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
        List<Ingredient> ingredients = List.of(new Ingredient("Egg", 2, "pcs", 0.0, LocalDate.MAX));
        new Recipe("Pannekake", null, "Pour batter in pan", ingredients, 2);
      });
      assertEquals("Recipe description cannot be null or blank", exception.getMessage());
    }

    @Test
    @DisplayName("Constructor throws IllegalArgumentException on blank description")
    public void testConstructor_throwsException_onBlankDescription() {
      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
        List<Ingredient> ingredients = List.of(new Ingredient("Egg", 2, "pcs", 0.0, LocalDate.MAX));
        new Recipe("Pannekake", " ", "Pour batter in pan", ingredients, 2);
      });
      assertEquals("Recipe description cannot be null or blank", exception.getMessage());
    }
  }

  @Test
  @DisplayName("Constructor throws IllegalArgumentException on null instruction")
  public void testConstructor_throwsException_onNullInstruction() {
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      List<Ingredient> ingredients = List.of(new Ingredient("Egg", 2, "pcs", 0.0, LocalDate.MAX));
      new Recipe("Pannekake", "Breakfast dish", null, ingredients, 2);
    });
    assertEquals("Recipe instructions cannot be null or blank", exception.getMessage());
  }

  @Test
  @DisplayName("Constructor throws IllegalArgumentException on blank instruction")
  public void testConstructor_throwsException_onBlankInstruction() {
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      List<Ingredient> ingredients = List.of(new Ingredient("Egg", 2, "pcs", 0.0, LocalDate.MAX));
      new Recipe("Pannekake", "Breakfast dish", " ", ingredients, 2);
    });
    assertEquals("Recipe instructions cannot be null or blank", exception.getMessage());
  }

  @Test
  @DisplayName("Constructor throws IllegalArgumentException on null list of ingredients")
  public void testConstructor_throwsException_onNullIngredients() {
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      new Recipe("Pannekake", "Breakfast dish", "Pour batter in pan", null, 2);
    });
    assertEquals("Recipe ingredients cannot be null or empty", exception.getMessage());
  }

  @Test
  @DisplayName("Constructor throws IllegalArgumentException on empty list of ingredients")
  public void testConstructor_throwsException_onEmptyIngredients() {
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      new Recipe("Pannekake", "Breakfast dish", "Pour batter in pan", new ArrayList<>(), 2);
    });
    assertEquals("Recipe ingredients cannot be null or empty", exception.getMessage());
  }

  @ParameterizedTest
  @ValueSource(ints = {0, -1})
  @DisplayName("Constructor throws IllegalArgumentException on negative or zero servings")
  public void testConstructor_throwsException_onNegativeOrZeroServings(int invalidServings) {
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      List<Ingredient> ingredients = List.of(new Ingredient("Egg", 2, "pcs", 0.0, LocalDate.MAX));
      new Recipe("Pannekake", "Breakfast dish", "Pour batter in pan", ingredients, invalidServings);
    });
    assertEquals("Servings must be greater than zero", exception.getMessage());
  }
}



