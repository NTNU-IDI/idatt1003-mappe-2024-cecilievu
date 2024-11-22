package edu.ntnu.idi.idatt;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Represents an ingredient with attributes such as name, quantity, unit, price and best-before date.
 */
public class Ingredient {
    // Attributes
    private final String nameItem;
    private double quantityItem;
    private final String unitItem;
    private final double pricePerUnit;
    private final LocalDate bestBefore;

    /**
     * Constructor that initializes an ingredient with the give attributes.
     */
    public Ingredient(String nameItem, double quantityItem, String unitItem, double pricePerUnit, LocalDate bestBefore) {
        this.nameItem = nameItem;
        this.quantityItem = quantityItem;
        this.unitItem = unitItem;
        this.pricePerUnit = pricePerUnit;
        this.bestBefore = bestBefore;
    }

    /**
     * Gets the name of the item.
     *
     * @return the name of the item
     */
    public String getNameItem() {
        return nameItem;
    }

    /**
     * Gets the quantity for the item.
     *
     * @return the quantity of the item
     */
    public double getQuantityItem() {
        return quantityItem;
    }

    /**
     * Sets the quantity of the item.
     *
     * @param quantityItem the new quantity of the item
     * @throws IllegalArgumentException if the quantity is negative
     */
    public void setQuantityItem(double quantityItem) {
        if (quantityItem > 0) {
            this.quantityItem = quantityItem;
        } else {
            throw new IllegalArgumentException("Quanity cannot be negative");
        }
    }

    /**
     * Gets the unit of the item.
     *
     * @return the unit of the item
     */
    public String getUnitItem() {
        return unitItem;
    }

    /**
     * Gets the price for the item.
     *
     * @return the price per unit of the item
     */
    public double getPricePerUnit() {
        return pricePerUnit;
    }

    /**
     * Gets the best-before date of the item.
     *
     * @return the best-before date
     */
    public LocalDate getBestBefore() {
        return bestBefore;
    }

    /**
     * Returns a string representation of the ingredient.
     *
     * @return a string that contains the name, quanity, price per unit and best-before date of the item.
     */
    @Override
    public String toString() {
        return "Name of item: " + nameItem + ", quantity: " + quantityItem + " " + unitItem + "\n"
                + "Price per unit: " + pricePerUnit + " kr," + " best-before-date: " + bestBefore + "\n";
    }

    /**
     * Compares the ingredient to another object to determine if they are euqal.
     * Two ingredients are considered equal if they have the same name,
     * the same best-before and the same price per unit
     *
     * @param o the object to compare the ingredient to
     * @return true if the specified object is equal to the ingredient; false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true; // Samme objekt, returner true
        if (o == null || getClass() != o.getClass()) return false; // Null eller annen klasse, returner false
        Ingredient that = (Ingredient) o; // Typecast til Ingredient
        return Double.compare(that.pricePerUnit, pricePerUnit) == 0 &&
                nameItem.equalsIgnoreCase(that.nameItem) && // Sammenlign navn (case-insensitive)
                bestBefore.equals(that.bestBefore); // Sammenlign best før-dato
    }

    /**
     * Generates a hash code for this ingredient.
     * The hash code is computed based on the same name, best-before date
     * and price per unit og the ingredient
     *
     * @return a hash code value of the ingredient
     */
    @Override
    public int hashCode() {
        return Objects.hash(nameItem.toLowerCase(), pricePerUnit, bestBefore);
    }
}