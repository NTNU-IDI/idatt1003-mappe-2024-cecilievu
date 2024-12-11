package edu.ntnu.idi.idatt.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test-klasse som skal sjekke om Ingredient-klassen funker
 *
 * <p>Disse metodene testes:</p>
 *
 * <b>Positive tester:</b>
 *
 * <ul>
 *     <li>Oppretter en vare med gyldig variabler (name, quantity, unit, pricePerUnit og bestBefore)</li>
 *     <li>Accessor-method skal returnere riktig navn</li>
 *     <li>Accessor-method skal returnere riktig quantity</li>
 *     <li>Accessor-method skal returnere riktig unit</li>
 * </ul>
 */

public class TestIngredient {

  Ingredient ingredient;

  /**
   * Sets up an ingredient with standard values. This method runs before each test.
   */
  @BeforeEach
  public void setUp() {
    ingredient = new Ingredient("Egg", 12, "pcs", 2, LocalDate.MAX);
  }

  /**
   * Negative tests.
   */
  @Nested
  @DisplayName("Negative tests for Ingredient, throws IllegalArgumentExceptions on wrong input")
  public class NegativeTests {

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", " "})
    @DisplayName("Constructor throws IllegalArgumentExceptions on null or blank name")
    public void testConstructor_throwsExceptions_onNullOrBlankName(String invalidName) {
      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
        new Ingredient(invalidName, 12, "pcs", 2, LocalDate.MAX);
      });
      assertEquals("Name cannot be null or blank", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    @DisplayName("Constructor throws IllegalArgumentExceptions on negative or zero quantity ")
    public void testConstructor_throwsExceptions_onZeroOrNegativeQuantity(int invalidQuantity) {
      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
        new Ingredient("Egg", invalidQuantity, "pcs", 2, LocalDate.MAX);
      });
      assertEquals("Quantity must be greater than zero", exception.getMessage());
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", " "})
    @DisplayName("Constructor throws IllegalArgumentExceptions on quantity ")
    public void testConstructor_throwsExceptions_onNullOrBlankUnit(String invalidUnit) {
      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
        new Ingredient("Egg", 12, invalidUnit, 2, LocalDate.MAX);
      });
      assertEquals("Unit cannot be null or blank", exception.getMessage());
    }

    @Test
    @DisplayName("Constructor throws IllegalArgumentExceptions on null best-before date")
    public void testConstructor_throwsIllegalArgumentExceptions_onNullBestBeforeDate() {
      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
        new Ingredient("Egg", 12, "pcs", 2, null);
      });
      assertEquals("Best-before date cannot be null", exception.getMessage());
    }

    @Test
    @DisplayName("setQuantityItem() throws IllegalArgumentException on negative value")
    public void testSetQuantityItem_throwsIllegalArgumentException_onNegativeValue() {
      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
        ingredient.setQuantityItem(-0.5);
      });
      assertEquals("Quantity cannot be negative", exception.getMessage());
    }
  }

  /**
   * Positive tests for Ingredient
   */
  @Nested
  @DisplayName("Positive test for Ingredient with valid input")
  public class methodsDoesNotThrowExceptions {

    @Test
    @DisplayName("Constructor successfully creates an item with valid input")
    public void testConstructor_createsItemWithValidInput() {
      Ingredient ingredient = new Ingredient("Egg", 12, "pcs", 2, LocalDate.MAX);
      assertEquals("Egg", ingredient.getNameItem());
      assertEquals(12, ingredient.getQuantityItem());
      assertEquals("pcs", ingredient.getUnitItem());
      assertEquals(2, ingredient.getPricePerUnit());
      assertEquals(LocalDate.MAX, ingredient.getBestBefore());
    }

    @Test
    @DisplayName("getNameItem() returns correct name")
    public void testGetNameItem_returnsCorrectName() {
      assertEquals("Egg", ingredient.getNameItem());
    }

    @Test
    @DisplayName("getQuantityItem() returns correct quantity")
    public void testGetQuantityItem_returnsCorrectQuantity() {
      assertEquals(12, ingredient.getQuantityItem());
    }

    @Test
    @DisplayName("getUnitItem() returns correct unit")
    public void testGetUnitItem_returnsCorrectUnit() {
      assertEquals("pcs", ingredient.getUnitItem());
    }

    @Test
    @DisplayName("getPricePerUni() returns correct price per unit")
    public void testGetPricePerUni_returnsCorrectPricePerUnit() {
      assertEquals(2, ingredient.getPricePerUnit());
    }

    @Test
    @DisplayName("getBestBefore() returns correct best-before date")
    public void testGetBestBefore_returnsCorrectBestBeforeDate() {
      assertEquals(LocalDate.MAX, ingredient.getBestBefore());
    }

    @Test
    @DisplayName("setQuantityItem() updates correctly to a positive value")
    public void testSetQuantityItem_updatesToPositiveValue() {
      ingredient.setQuantityItem(3);
      assertEquals(3, ingredient.getQuantityItem());
    }
  }
}