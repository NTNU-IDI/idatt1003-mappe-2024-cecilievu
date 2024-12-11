package edu.ntnu.idi.idatt.models;

import java.time.LocalDate;

/**
 * Represents an ingredient with attributes such as name, quantity, unit, price and best-before
 * date.
 */
public class Ingredient {

  // Attributes
  private final String nameItem;
  private double quantityItem;
  private final String unitItem;
  private final double pricePerUnit;
  private final LocalDate bestBefore;

  /**
   * Constructor that initializes an ingredient/item with the give attributes.
   *
   * @param nameItem     the name of the item
   * @param quantityItem the quantity of the item
   * @param unitItem     the unit of the item
   * @param pricePerUnit the price per unit of the item
   * @param bestBefore   the expiry date of the item
   * @throws IllegalArgumentException if the name is null or empty, if the quantity is negative, if
   *                                  the unit is null or empty or if the date is null.
   */
  public Ingredient(String nameItem, double quantityItem, String unitItem, double pricePerUnit,
      LocalDate bestBefore) {
    if (nameItem == null || nameItem.isBlank()) {
      throw new IllegalArgumentException("Name cannot be null or empty");
    }

    if (quantityItem <= 0) {
      throw new IllegalArgumentException("Quantity must be greater than zero");
    }

    if (unitItem == null || unitItem.isBlank()) {
      throw new IllegalArgumentException("Unit cannot be null or empty");
    }

    if (bestBefore == null) {
      throw new IllegalArgumentException("Best before cannot be null");
    }

    this.nameItem = nameItem;
    this.quantityItem = quantityItem;
    this.unitItem = unitItem;
    this.pricePerUnit = pricePerUnit;
    this.bestBefore = bestBefore;
  }

  /**
   * Returns the name of the item.
   *
   * @return the name of the item
   */
  public String getNameItem() {
    return nameItem;
  }

  /**
   * Returns the quantity for the item.
   *
   * @return the quantity of the item
   */
  public double getQuantityItem() {
    return quantityItem;
  }

  /**
   * Updates the quantity of the item. If the quantity is negative, an exception will be thrown.
   *
   * @param quantityItem the new quantity of the ingredient
   * @throws IllegalArgumentException if the quantity is negative
   */
  public void setQuantityItem(double quantityItem) {
    if (quantityItem < 0) {
      throw new IllegalArgumentException("Quantity cannot be negative");
    }
    this.quantityItem = quantityItem;
  }

  /**
   * Returns the unit of the item.
   *
   * @return the unit of the item
   */
  public String getUnitItem() {
    return unitItem;
  }

  /**
   * Returns the price for the item.
   *
   * @return the price per unit of the item
   */
  public double getPricePerUnit() {
    return pricePerUnit;
  }

  /**
   * Returns the best-before date of the item.
   *
   * @return the best-before date of the item
   */
  public LocalDate getBestBefore() {
    return bestBefore;
  }
}