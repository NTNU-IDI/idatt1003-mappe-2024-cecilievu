package edu.ntnu.idi.idatt.models;

import java.time.LocalDate; //Newer version than util.date (help from Co-pilot)
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * This class represent a "food storage" that manages items in a fridge.
 */
public class FoodStorage {
    /**
     * A list of items in the fridge.
     */
    private final List<Ingredient> items;

    /**
     * Constructor that initializes the food storage with an empty list of items.
     */
    public FoodStorage() {
        this.items = new ArrayList<>();
    }

    //Got help from Co-pilot to change the methods from for/each loop to stream to make the code
    //shorter but also more readable

    /**
     * Display items in the fridge in a copy of the ingredient list.
     * This makes sure that the list is immutability.
     */
    public List<Ingredient> getItems() {
        return new ArrayList<>(items);
    }

    /**
     * Adds new items to the food storage.
     * (ifPresentOrElse) if an item has the same name, best before date and price,
     * it will be added to an existing item and their quantities.
     * Otherwise, the item will be added as a new entry.
     *
     * @param newItem the item to be added
     */
    public String addItem(Ingredient newItem) {
        if (newItem == null || newItem.getQuantityItem() <= 0) {
            throw new IllegalArgumentException("Invalid item or quantity \n");
        }
        items.stream()
                .filter(item -> item.getNameItem().equalsIgnoreCase(newItem.getNameItem())
                        && item.getBestBefore().equals(newItem.getBestBefore())
                        && item.getPricePerUnit() == newItem.getPricePerUnit())
                .findFirst()
                .ifPresentOrElse(
                        item -> item.setQuantityItem(item.getQuantityItem() + newItem.getQuantityItem()), // Updates quantity
                        () -> items.add(newItem) // Add new item
                );
        return String.format("%.2f %s of %s has been added to the fridge! \n", newItem.getQuantityItem(),newItem.getUnitItem(), newItem.getNameItem());
    }

    /**
     * Removes a specific item from the fridge, starting with the earliest expiry date.
     * CurrentModificationExceptions
     *
     * @param name the name of the item to remove
     * @param quantity the quantity of the item to remove
     * @throws IllegalArgumentException if the quantity is greater than total quantity of the item
     */
    public String removeItem(String name, double quantity) {
        double remainingQuantity = quantity;

        items.sort(Comparator.comparing(Ingredient::getBestBefore)); // Sorterer varer etter best-before date
        StringBuilder result = new StringBuilder();

        for (Ingredient item : items) {
            if (remainingQuantity <= 0) break; // Hvis alt blir fjernet bryter man ut av løkken

            if (item.getNameItem().equalsIgnoreCase(name)) {
                double amountToRemove = Math.min(remainingQuantity, item.getQuantityItem()); // Hvor mye vi kan fjerne fra varen
                item.setQuantityItem(item.getQuantityItem() - amountToRemove); // Oppdaterer mengden i stock
                remainingQuantity -= amountToRemove; // Oppdaterer hvor mye vi fortsatt må fjerne

                result.append(String.format("%.2f %s of %s with best before %s is removed. Remaining in stock: %.2f\n", amountToRemove, item.getUnitItem(), item.getNameItem(), item.getBestBefore(), item.getQuantityItem()));
            }
        }

        // Hvis ikke nok i stock for å fjerne ønsket mengde
        if (remainingQuantity > 0) {
            result.append(String.format("Not enough %s in stock to remove %.2f. Stock in fridge: %.2f.", name, quantity, remainingQuantity));
        }

        items.removeIf(item -> item.getQuantityItem() <= 0);
        return result.toString();

        //return String.format("%.2f of %s is removed. Item is now out of stock.", quantity, name);
    }

    /**
     * Searching for a specific item by name in the fridge.
     * .collect will make sure the found items will be displayed
     * in a new ArrayList (copy).
     * The displayed list will show the found items by date
     *
     * @return the item if found, or null if not
     */
    public List<Ingredient> searchItem(String name){
        return new ArrayList<>( // 2. making it mutable from toList()
                items.stream()
                .filter(item -> item.getNameItem().equalsIgnoreCase(name))
                .sorted(Comparator.comparing(Ingredient::getBestBefore))
                .toList() // 1. making an immutable list
        );
    }

    /**
     * Searching for ingredients that expires by a specific date.
     * .toList() it immutable
     *
     * @param date the specific date
     * @return ingredients that expires before the date
     */
    public List<Ingredient> getItemsBeforeDate(LocalDate date) {
        return new ArrayList<>( // 2. making it mutable from toList()
                items.stream()
                        .filter(item -> item.getBestBefore().isBefore(date))
                        .sorted(Comparator.comparing(Ingredient::getBestBefore))
                        .toList() // 1. making an immutable list
        );
    }

    //Chat
    /**
     * Shows expired items along with their total value
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
     * Calculates the total value of all items in the fridge
     *
     * @return the total value
     */
    public double calculateTotalValue() {
        return items.stream()
                .mapToDouble(item -> item.getQuantityItem() * item.getPricePerUnit())
                .sum();
    }
}

