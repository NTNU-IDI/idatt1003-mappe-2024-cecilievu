package edu.ntnu.idi.idatt;

import java.time.LocalDate; //Newer version than util.date (help from Co-pilot)
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;

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
     * If an item has the same name, best before date and price, it will be added to an existing item and their quantities.
     * Otherwise, the item will be added as a new entry.
     *
     * @param newItem the item to be added
     */
    public void addItem(Ingredient newItem) {
        boolean itemExsist = false;
        for (Ingredient item : items) {
            if (item.getNameItem().equalsIgnoreCase(newItem.getNameItem()) && item.getBestBefore().equals(newItem.getBestBefore()) && item.getPricePerUnit() == newItem.getPricePerUnit()) {
                item.setQuantityItem(item.getQuantityItem() + newItem.getQuantityItem());
                System.out.println(newItem.getQuantityItem() + " " + newItem.getUnitItem() + " of " + newItem.getNameItem() + " has been added to exsisting item in the fridge.");
                itemExsist = true;
                break;
            }
        }
        if (!itemExsist) {
            items.add(newItem);
            System.out.println(newItem.getNameItem() + " has been added to the fridge.");
        }
    }

    /**
     * Displays all items in the fridge
     */
    public void showItem() {
        if (items.isEmpty()) {
            System.out.println("The fridge is empty");
        } else {
            System.out.println("Items in the fridge:");
            for (Ingredient item : items) {
                System.out.println(item);
            }
        }
    }

    /**
     * Sorting items in the fridge by name and date
     */
    public void sortItemByNameAndDate() {
        items.sort((Comparator.comparing(Ingredient::getNameItem))
                .thenComparing(Ingredient::getBestBefore));
    }

    /**
     * Searching for a specific item in the fridge
     *
     * @param name the name of the item to search for
     * @return the item if found, or null if not
     */
    public Ingredient searchItem(String name) {
        for (Ingredient item : items) {
            if (item.getNameItem().equalsIgnoreCase(name)) { //equalsIgnoreCase gjør den case-insensitiv
                System.out.println("Item found in the fridge: " + item.getNameItem() + ", quantity of item: "
                        + item.getQuantityItem() + " " + item.getUnitItem());
                return item;
            }
        }
        //System.out.println("Item not found in fridge: " + name);
        return null;
    }

    /**
     * Optional to find the latest added item with the specified name.
     *
     * @param name the name of the item
     * @return an Optional containing the latest item, or empty if not found
     */
    public Optional<Ingredient> findLatestItem(String name) {
        return items.stream()
                .filter(item -> item.getNameItem().equalsIgnoreCase(name)) //lamda-uttrykk, obs sjekk ut mer om dette
                .reduce((first, second) -> second); //betyr at vi alltid beholder det siste elementet vi finner
    }

    /**
     * Removes a specific item from the fridge, starting with the earliest expiry date.
     *
     * @param name     the name of the item to remove
     * @param quantity the quantity of the item to remove
     */
    public void removeItem(String name, double quantity) {
        double remainingQuantity = quantity;
        items.sort(Comparator.comparing(Ingredient::getBestBefore)); //sortere items by best before
        for (Ingredient item : items) {
            if (remainingQuantity <= 0) break;

            //ny løkke som itererer gjennom de ulike items for å ta ut varen som går ut av dato førts
            for (Ingredient items : items) {
                if (remainingQuantity <= 0) break;

                if (item.getNameItem().equalsIgnoreCase(name)) {
                    if (item.getQuantityItem() >= remainingQuantity) {
                        item.setQuantityItem(item.getQuantityItem() - remainingQuantity);

                    }
                }
            }
        }
    }

    /**
     * Shows expired items along with their total value
     */
    public void showExpiredItems() {
        LocalDate today = LocalDate.now();
        double totalValue = 0; //lokale verdier
        boolean hasExpiredItems = false; //lokale verdier
        System.out.println("Expired items: ");
        for (Ingredient item : items) {
            if (item.getBestBefore().isBefore(today)) {
                System.out.println(item); //printer ut varene som er gått ut av dato
                totalValue += item.getQuantityItem() * item.getPricePerUnit(); //beregner verdien av varene som har gått ut av dato
                hasExpiredItems = true;
            }
        }
        if (!hasExpiredItems) {
            System.out.println("No items have expired! :)");
        } else {
            System.out.println("Total value of expired items: " + totalValue + " kr");
            System.out.println("Before throwing out, LOOK - SMELL - TASTE! Trust your senses, reduce foodwaste! :)");
        }
    }

    /**
     * Calculates the total value of all items in the fridge
     *
     * @return the total valye
     */
    public double calculateTotalValue() {
        double totalValue = 0;
        for (Ingredient item : items) {
            totalValue += item.getQuantityItem() * item.getPricePerUnit();
        }
        return totalValue;
    }
}
