package edu.ntnu.idi.idatt;

import java.time.LocalDate; //Newer version than util.date (help from Co-pilot)
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Optional;
import java.util.stream.*;

/**
 * This class represent a "food storage" that manages items in a fridge.
 */
public class FoodStorage {
    /**
     * A list of items in the fridge.
     */
    private ArrayList<Ingredient> items;

    /**
     * Constructor that initializes the food storage with an empty list of items.
     */
    public FoodStorage() {
        this.items = new ArrayList<>();
    }

    //Co-pilot

    /**
     * Adds new items to the food storage.
     * If an item has the same name, best before date and price,
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
                + " of " + newItem.getNameItem()
                + (items.contains(newItem) ? " has been added to the fridge!" : " has been added to an existing item in the fridge!"));
    }

    /**
     * Display items in the fridge in a copy of the ingredient list.
     */
    public ArrayList<Ingredient> getItems() {
        return new ArrayList<>(items);
    }

    /**
     * Searching for a specific item in the fridge
     *
     * @param name the name of the item to search for
     * @return the item if found, or null if not
     */
    public Ingredient searchItem(String name){
        return items.stream()
                .filter(item -> item.getNameItem().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    /**
     * Searching for ingredients that expires by a specific date.
     *
     * @param date the specific date
     * @return ingredients that expires before the date
     */
    public ArrayList<Ingredient> searchItemByDate(LocalDate date) {
        return new ArrayList<>(
                items.stream()
                        .filter(item -> item.getBestBefore().isBefore(date))
                        .sorted(Comparator.comparing(Ingredient::getBestBefore))
                        .toList()
        );
    }

    /**
     * Removes a specific item from the fridge, starting with the earliest expiry date.
     *
     * @param name the name of the item to remove
     * @param quantity the quantity of the item to remove
     */
    public void removeItem(String name, double quantity){
        double remainingQuantity = quantity;

        items.sort(Comparator.comparing(Ingredient::getBestBefore));
        Iterator<Ingredient> iterator = items.iterator();
        while(iterator.hasNext() && remainingQuantity > 0){
            Ingredient item = iterator.next();
            if(item.getNameItem().equalsIgnoreCase(name)){
                if(item.getQuantityItem() > remainingQuantity){
                    item.setQuantityItem(item.getQuantityItem() - remainingQuantity);
                    System.out.printf("%.2f of %s is removed. Remaining in stock: %.2f%n", quantity, name, item.getQuantityItem());
                    return;
                } else {
                    remainingQuantity -= item.getQuantityItem();
                    System.out.printf("%.2f of %s is removed.%n", item.getQuantityItem(), name);
                    iterator.remove();
                }
            }
        }
        if(remainingQuantity > 0){}
        System.out.printf("Not enough %s in stock to remove %.2f. Missing %.2f.%n", name, quantity, remainingQuantity);
    }

    //Chat
    /**
     * Shows expired items along with their total value
     */
    public ArrayList<Ingredient> getExpiredItems() {
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

