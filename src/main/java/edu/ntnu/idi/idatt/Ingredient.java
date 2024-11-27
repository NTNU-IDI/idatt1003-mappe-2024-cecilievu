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
}