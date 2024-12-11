package edu.ntnu.idi.idatt.models;

import java.time.LocalDate; //Newer version than util.date (help from Co-pilot)
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Represent the "food storage" of the application that manages items in a fridge. It allows users
 * to add, remove, search and display items stored in the fridge.
 */
public class FoodStorage {

  /**
   * A list of ingredients in the fridge. This list stores all items that have been added by the
   * user.
   */
  private final List<Ingredient> items;

  /**
   * Constructor that initializes the food storage with an empty list of items.
   */
  public FoodStorage() {
    this.items = new ArrayList<>();
  }

  // Fikk hjelp fra Co-pilot til å endre metodene fra for/each loop til stream hvor det er nødvendig
  // Dette for å gjøre koden mer robust, kortere og lettere å lese.

  /**
   * Returns a copy of all items in the fridge. Changes to the returned list do not affect the
   * original list.
   *
   * @return a copy of the list of items in the fridge.
   */
  public List<Ingredient> getItems() {
    return new ArrayList<>(items);
  }

  /**
   * Adds new items to the food storage. If an item with the same name, expiry date and price
   * already exist, the quantities are combined. Otherwise, the item will be added as a new entry.
   *
   * @param newItem the item to be added.
   * @return a message that the item have been added successfully.
   * @throws IllegalArgumentException if the item name is null or quantity is zero or negative.
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

  // Hjelp fra ChatGPT

  /**
   * Removes a specified quantity of an item from the fridge, starting with the earliest expiry
   * date. If an item's stock reaches zero, it will be removed from the list.
   *
   * @param name     the name of the item to remove
   * @param quantity the quantity of the item to remove
   * @return a summary of the removing process, including details about removed items and updated
   * stock status.
   */
  public String removeItem(String name, double quantity) {
    double remainingQuantity = quantity;
    items.sort(Comparator.comparing(Ingredient::getBestBefore)); // Sorterer varer etter dato
    StringBuilder result = new StringBuilder();

    for (Ingredient item : items) {
      if (remainingQuantity <= 0) {
        break; // Hvis alt blir fjernet bryter man ut av løkken
      }

      // Hvis det finnes flere varer med samme navn i kjøleskapet.
      if (item.getNameItem().equalsIgnoreCase(name)) {
        // Hvor mye vi kan fjerne (fjerner varen som går ut først)
        double amountToRemove = Math.min(remainingQuantity, item.getQuantityItem());
        item.setQuantityItem(item.getQuantityItem() - amountToRemove); // Oppdaterer mengden i stock
        remainingQuantity -= amountToRemove; // Oppdaterer hvor mye vi fortsatt må fjerne

        result.append(String.format(
            "%.2f %s of %s with best before %s is removed. Remaining in stock: %.2f\n",
            amountToRemove, item.getUnitItem(), item.getNameItem(), item.getBestBefore(),
            item.getQuantityItem()
        ));
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
   * Searching for items by name in the fridge and returns a sorted list of matches.
   *
   * @return the item if found, or null if not
   */
  public List<Ingredient> searchItem(String name) {
    return new ArrayList<>(// 2. gjør den mutable fra toList()
        items.stream()
            .filter(item -> item.getNameItem().equalsIgnoreCase(name))
            .sorted(Comparator.comparing(Ingredient::getBestBefore))
            .toList() // 1. lager en immutable liste
    );
  }

  /**
   * Retrieves a list of ingredients that expire on or before the specified date. The returned list
   * is sorted by the increasing expiry date.
   *
   * @param date the specific expiry date.
   * @return a list ingredients that expires on or before the specified date.
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

  /**
   * Shows expired items along with their total value.
   *
   * @return the list of expired items and its total value.
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
   * @return the total value.
   */
  public double calculateTotalValue() {
    return items.stream()
        .mapToDouble(item -> item.getQuantityItem() * item.getPricePerUnit())
        .sum();
  }
}

