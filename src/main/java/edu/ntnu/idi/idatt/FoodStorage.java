package edu.ntnu.idi.idatt;

import java.time.LocalDate; //Newer version than util.date (help from Co-pilot)
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class represent a "food storage" that manages items in a fridge.
 */
public class FoodStorage {
    /**
     * A list of items in the fridge.
     */
    private List<Ingredient> items;

    /**
     * Constructor that initializes the food storage with an empty list of items.
     */
    public FoodStorage() {
        this.items = new ArrayList<>();
    }

    //Got help from Co-pilot to change the methods from for/each loop to stream to make the code
    //shorter but also more readable

    /**
     * Adds new items to the food storage.
     * (ifPresentOrElse) if an item has the same name, best before date and price,
     * it will be added to an existing item and their quantities.
     * Otherwise, the item will be added as a new entry.
     *
     * @param newItem the item to be added
     */
    public void addItem(Ingredient newItem) {
        items.stream()
                .filter(item -> item.getNameItem().equalsIgnoreCase(newItem.getNameItem())
                        && item.getBestBefore().equals(newItem.getBestBefore())
                        && item.getPricePerUnit() == newItem.getPricePerUnit())
                .findFirst()
                .ifPresentOrElse(
                        item -> item.setQuantityItem(item.getQuantityItem() + newItem.getQuantityItem()), // Updates quantity
                        () -> items.add(newItem) // Add new item
                );

        System.out.println(newItem.getQuantityItem() + " " + newItem.getUnitItem()
                + " of " + newItem.getNameItem() + " has been added to the fridge!");
    }

    /**
     * Display items in the fridge in a copy of the ingredient list.
     * This makes sure that the list is immutability.
     */
    public List<Ingredient> getItems() {
        return new ArrayList<>(items);
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
    public List<Ingredient> searchItemByDate(LocalDate date) {
        return new ArrayList<>( // 2. making it mutable from toList()
                items.stream()
                        .filter(item -> item.getBestBefore().isBefore(date))
                        .sorted(Comparator.comparing(Ingredient::getBestBefore))
                        .toList() // 1. making an immutable list
        );
    }

    /**
     * Removes a specific item from the fridge, starting with the earliest expiry date.
     * CurrentModificationExceptions
     *
     * @param name the name of the item to remove
     * @param quantity the quantity of the item to remove
     */
    public String removeItem(String name, double quantity) {
        double remainingQuantity = quantity;

        items.sort(Comparator.comparing(Ingredient::getBestBefore));
        Iterator<Ingredient> iterator = items.iterator();

        while (iterator.hasNext() && remainingQuantity > 0) {
            Ingredient item = iterator.next();
            if (item.getNameItem().equalsIgnoreCase(name)) {
                if (item.getQuantityItem() > remainingQuantity) {
                    item.setQuantityItem(item.getQuantityItem() - remainingQuantity);
                    return String.format("%.2f of %s is removed. Remaining in stock: %.2f%n", quantity, name, item.getQuantityItem());
                }
                iterator.remove();
                return String.format("%.2f of %s is removed. Item is now out of stock.", item.getQuantityItem(), name);
            }
        }
        return String.format("Not enough %s in stock to remove %.2f. Missing %.2f.", name, quantity, remainingQuantity);
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
     * @return the total valye
     */
    public double calculateTotalValue() {
        return items.stream()
                .mapToDouble(item -> item.getQuantityItem() * item.getPricePerUnit())
                .sum();
    }
}

