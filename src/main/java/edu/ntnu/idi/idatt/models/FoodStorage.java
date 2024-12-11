package edu.ntnu.idi.idatt.models;

import java.time.LocalDate; //Newer version than util.date (help from Co-pilot)
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * This class represent the "food storage" of the application that manages items in a fridge. It
 * allows the user among other things such ass add, remove, search or display items.
 */
public class FoodStorage {

  /**
   * A list of items in the fridge. This list stores all the items that have been added by the
   * user.
   */
  private final List<Ingredient> items;

  /**
   * Constructor that initializes the food storage with an empty list of items.
   */
  public FoodStorage() {
    this.items = new ArrayList<>();
  }

  // Got help from Co-pilot to change the methods from for/each loop to stream where needed
  // to make the code shorter but also more readable

  /**
   * Returns a list of all items in the fridge. This method creates a copy of the list of items to
   * avoid external changes from affecting the original list in the fridge.
   *
   * @return a copy of the list of items in the fridge.
   */
  public List<Ingredient> getItems() {
    return new ArrayList<>(items);
  }

  /**
   * Adds new items to the food storage. (ifPresentOrElse) if an item has the same name, best before
   * date and price, it will be added to an existing item and their quantities. Otherwise, the item
   * will be added as a new entry.
   *
   * @param newItem the item to be added.
   * @return a message that the item have been added successfully.
   * @throws IllegalArgumentException if the item name is null or quantity is 0 or negative.
   */
  public String addItem(Ingredient newItem) {
    if (newItem == null || newItem.getQuantityItem() <= 0) {
      throw new IllegalArgumentException("Invalid item or quantity");
    }
    items.stream()
        .filter(item -> item.getNameItem().equalsIgnoreCase(newItem.getNameItem())
            && item.getBestBefore().equals(newItem.getBestBefore())
            && item.getPricePerUnit() == newItem.getPricePerUnit())
        .findFirst()
        .ifPresentOrElse(
            item -> item.setQuantityItem(item.getQuantityItem() + newItem.getQuantityItem()),
            // Oppdaterer mengden
            () -> items.add(newItem) // Legger til ny vare
        );
    return String.format("%.2f %s of %s has been added to the fridge!",
        newItem.getQuantityItem(), newItem.getUnitItem(), newItem.getNameItem());
  }

  /**
   * Removes a specific item from the fridge, starting with the earliest expiry date.
   * CurrentModificationExceptions
   *
   * @param name     the name of the item to remove
   * @param quantity the quantity of the item to remove
   * @throws IllegalArgumentException if the quantity is greater than total quantity of the item
   */
  public String removeItem(String name, double quantity) {
    double remainingQuantity = quantity;

    items.sort(
        Comparator.comparing(Ingredient::getBestBefore)); // Sorterer varer etter best-before date
    StringBuilder result = new StringBuilder();

    for (Ingredient item : items) {
      if (remainingQuantity <= 0) {
        break; // Hvis alt blir fjernet bryter man ut av løkken
      }

      if (item.getNameItem().equalsIgnoreCase(name)) {
        double amountToRemove = Math.min(remainingQuantity,
            item.getQuantityItem()); // Hvor mye vi kan fjerne fra varen
        item.setQuantityItem(item.getQuantityItem() - amountToRemove); // Oppdaterer mengden i stock
        remainingQuantity -= amountToRemove; // Oppdaterer hvor mye vi fortsatt må fjerne

        result.append(String.format(
            "%.2f %s of %s with best before %s is removed. Remaining in stock: %.2f\n",
            amountToRemove, item.getUnitItem(), item.getNameItem(), item.getBestBefore(),
            item.getQuantityItem()));
      }
    }

    // Hvis ikke nok i stock for å fjerne ønsket mengde
    if (remainingQuantity > 0) {
      result.append(String.format("Not enough %s in stock to remove %.2f. Stock in fridge: %.2f.",
          name, quantity, remainingQuantity));
    }
    items.removeIf(item -> item.getQuantityItem() <= 0);
    return result.toString();
  }

  /**
   * Searching for a specific item by name in the fridge. .collect will make sure the found items
   * will be displayed in a new ArrayList (copy). The displayed list will show the found items by
   * date
   *
   * @return the item if found, or null if not
   */
  public List<Ingredient> searchItem(String name) {
    return new ArrayList<>(// 2. making it mutable from toList()
        items.stream()
            .filter(item -> item.getNameItem().equalsIgnoreCase(name))
            .sorted(Comparator.comparing(Ingredient::getBestBefore))
            .toList() // 1. making an immutable list
    );
  }

  /**
   * Searching for ingredients that expires by a specific date. .toList() it immutable
   *
   * @param date the specific date
   * @return ingredients that expires before the date
   */
  public List<Ingredient> getItemsBeforeDate(LocalDate date) {
    return new ArrayList<>(// 2. making it mutable from toList()
        items.stream()
            .filter(item -> {
              LocalDate bestBefore = item.getBestBefore();
              return !bestBefore.isAfter(date); // Sjekker varer før eller samme dato
            })
            .sorted(Comparator.comparing(Ingredient::getBestBefore))
            .toList() // 1. making an immutable list
    );
  }

  //Chat

  /**
   * Shows expired items along with their total value.
   */
  public List<Ingredient> getExpiredItems() {
    LocalDate today = LocalDate.now();
    return new ArrayList<>(
        items.stream()
            .filter(item -> item.getBestBefore().isBefore(today))
            .sorted(Comparator.comparing(Ingredient::getBestBefore))
            .toList()
    );
  }

  /**
   * Calculates the total value of all items in the fridge.
   *
   * @return the total value
   */
  public double calculateTotalValue() {
    return items.stream()
        .mapToDouble(item -> item.getQuantityItem() * item.getPricePerUnit())
        .sum();
  }
}

