package edu.ntnu.idi.idatt.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class TestFoodStorage {

  FoodStorage foodStorage;

  @BeforeEach
  public void setUp() {
    foodStorage = new FoodStorage();
    Ingredient ingredient1 = new Ingredient("Egg", 12, "pcs", 2.0, LocalDate.of(2024, 12, 24));
    Ingredient ingredient2 = new Ingredient("Milk", 3, "dL", 10.0, LocalDate.of(2024, 12, 20));
    Ingredient ingredient3 = new Ingredient("Butter", 250, "grams", 0.1,
        LocalDate.of(2024, 12, 10));
    foodStorage.addItem(ingredient1);
    foodStorage.addItem(ingredient2);
    foodStorage.addItem(ingredient3);
  }

  /**
   * Negative test
   */
  @Nested
  @DisplayName("Negative test for the FoodStorage, throws IllegalArgumentException or returns error on wrong input")
  public class Negative {

    @Test
    @DisplayName("addItem() throws IllegalArgumentException for invalid item or quantity")
    public void testAddItem_throwsExceptionForInvalidItem() {
      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
        foodStorage.addItem(null);
      });
      assertEquals("Invalid item or quantity", exception.getMessage());
    }

    @Test
    @DisplayName("removeItem() returns error if there is not enough items in stock")
    public void testRemoveItem_returnsErrorIfNotEnoughItems() {
      String result = foodStorage.removeItem("Egg", 20);
      assertTrue(result.contains("Not enough Egg in stock"));
    }
  }

  @Nested
  @DisplayName("Positive tests for the FoodStorage with valid input")
  public class Positive {

    @Test
    @DisplayName("addItem() successfully add an item")
    public void testAddItem_successfullyAddItem() {
      Ingredient ingredient4 = new Ingredient("Apple", 5, "pcs", 5.0, LocalDate.of(2024, 12, 24));
      foodStorage.addItem(ingredient4);
      assertTrue(
          foodStorage.getItems().stream().anyMatch(item -> item.getNameItem().equals("Apple")));
    }

    @Test
    @DisplayName("addItem() combines quantities for existing items, must be same name, unit, price per unit and date")
    public void testAddItem_combineQuantitiesForExistingItem() {
      Ingredient additional = new Ingredient("Egg", 5, "pcs", 2.0, LocalDate.of(2024, 12, 24));
      foodStorage.addItem(additional);
      Ingredient egg = foodStorage.searchItem("Egg").getFirst();
      assertEquals(17, egg.getQuantityItem()); // 12 + 5 = 17
    }

    @Test
    @DisplayName("removeItem() successfully removes an item")
    public void testRemoveItem_successfullyRemoveItem() {
      foodStorage.removeItem("Egg", 6);
      Ingredient egg = foodStorage.searchItem("Egg").getFirst();
      assertEquals(6, egg.getQuantityItem()); // 12 - 6 = 6
    }

    @Test
    @DisplayName("searchItem() returns the correct list of items")
    public void testSearchItem_returnsCorrectList() {
      List<Ingredient> foundItem = foodStorage.searchItem("Egg");
      assertEquals(1, foundItem.size());
      assertEquals("Egg", foundItem.get(0).getNameItem());
    }

    @Test
    @DisplayName("getItemsBeforeDate() returns items expiring before date")
    public void testGetItemsBeforeDate_returnsExpiringItems() {
      List<Ingredient> items = foodStorage.getItemsBeforeDate(LocalDate.of(2024, 12, 11));
      assertEquals(1, items.size()); // Smør går ut på dato 10-12-2024
      assertEquals("Butter", items.get(0).getNameItem());
    }

    @Test
    @DisplayName("getExpiredItems() returns expired items")
    public void testGetExpiredItems_returnsExpiredItems() {
      foodStorage.addItem(
          new Ingredient("Cheese", 150, "grams", 0.2, LocalDate.now().minusDays(1)));
      List<Ingredient> expiredItems = foodStorage.getExpiredItems();
      assertTrue(expiredItems.stream().anyMatch(item -> item.getNameItem().equals("Cheese")));
    }

    @Test
    @DisplayName("calculateTotalValue() calculates total value correctly")
    public void testCalculateTotalValue_returnsCorrectTotalValue() {
      double totalValue = foodStorage.calculateTotalValue();
      double expectedValue = (12 * 2) + (3 * 10) + (250 * 0.1); // Summere egg, melk og smør
      assertEquals(expectedValue, totalValue,
          0.001); // Delta for å godkjenne avrundingsfeil opp til 0.001
    }
  }
}