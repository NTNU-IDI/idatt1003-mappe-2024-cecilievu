package edu.ntnu.idi.idatt;

import java.time.LocalDate; //Newer version than util.date (help from Co-pilot)
import java.util.ArrayList;
import java.util.Comparator;
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
     * If an item has the same name, best before date and price, it will be added to an existing item and their quantities.
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

        System.out.println(newItem.getQuantityItem() + " " + newItem.getUnitItem() +
                " of " + newItem.getNameItem() +
                (items.contains(newItem) ? " has been added to an existing item in the fridge." : " has been added to the fridge."));
    }

    /**
     * Displays all items in the fridge,
     * sorted after name and date
     */
    public void showItem() {
        if (items.isEmpty()) {
            System.out.println("The fridge is empty");
        } else {
            System.out.println("Items in the fridge:");
            System.out.println();
            System.out.println("Name     | Quantity  Unit    | Price per unit | Best before date   ");
            System.out.println("----------------------------------------------------------------");
            items.stream()
                    .sorted(Comparator.comparing(Ingredient::getNameItem)
                            .thenComparing(Ingredient::getBestBefore))
                    .forEach(item -> {
                        String formatted = String.format("%-8s | %7.2f   %-7s | %6.2f kr      | %4s",
                                item.getNameItem(), item.getQuantityItem(), item.getUnitItem(), item.getPricePerUnit(), item.getBestBefore()
                        );
                        System.out.println(formatted);
                    });
            System.out.println();
        }
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
     * Optional to find the latest added item with the specified name.
     *
     * @param name the name of the item
     * @return an Optional containing the latest item, or empty if not found
     */
    public Optional<Ingredient> findLatestItem (String name){
        return items.stream()
                .filter(item -> item.getNameItem().equalsIgnoreCase(name))
                .reduce((first, second) -> second); // betyr at vi alltid beholder det siste elementet vi finner
    }

    /**
     * Removes a specific item from the fridge, starting with the earliest expiry date.
     *
     * @param name the name of the item to remove
     * @param quantity the quantity of the item to remove
     */
    public void removeItem(String name, double quantity){
        double remainingQuantity = quantity;
        items.sort(Comparator.comparing(Ingredient::getBestBefore)); //sortere items by best before
        for (Ingredient item : items) {
            if (remainingQuantity <= 0) break;

            //ny løkke som itererer gjennom de ulike items for å ta ut varen som går ut av dato først
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

    //Chat
    /**
     * Shows expired items along with their total value
     */
    public void showExpiredItems() {
        LocalDate today = LocalDate.now();

        ArrayList<Ingredient> expiredItems = items.stream()
                .filter(item -> item.getBestBefore().isBefore(today)) // Filtrerer ut varer som har gått ut av dato
                .collect(Collectors.toCollection(ArrayList::new)); // Samler de i en ArrayList
        if (expiredItems.isEmpty()) {
            System.out.println("No items have expired! :)");
        } else {
            System.out.println("Expired items:");
            expiredItems.forEach(System.out::println); // Skriver ut alle utgåtte varer

            double totalValue = expiredItems.stream()
                    .mapToDouble(item -> item.getQuantityItem() * item.getPricePerUnit())
                    .sum();
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
        return items.stream()
                .mapToDouble(item -> item.getQuantityItem() * item.getPricePerUnit())
                .sum();
    }
}

