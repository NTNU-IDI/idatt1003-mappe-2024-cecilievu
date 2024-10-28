package edu.ntnu.idi.idatt;

import java.time.LocalDate;

public class Ingredient {
    //attributes
    private String nameItem;
    private double quantityItem;
    private String unitItem;
    private double pricePerUnit;
    private LocalDate bestBefore;

    /**
     * Konstruktør som oppretter this. objekter
     */
    public Ingredient(String nameItem, double quantityItem, String unitItem, double pricePerUnit, LocalDate bestBefore) {
        this.nameItem = nameItem;
        this.quantityItem = quantityItem;
        this.unitItem = unitItem;
        this.pricePerUnit = pricePerUnit;
        this.bestBefore = bestBefore;
    }

    /**
     * Henter navnet på varen
     * @return navnet på varen
     */
    public String getNameItem() {
        return nameItem;
    }

    /**
     * Setter navn på varen
     * @param nameItem navnet på varen
     */
    public void setNameItem(String nameItem) {
        this.nameItem = nameItem;
    }

    /**
     * Henter mengde for varen
     * @return mengde av varen
     */
    public double getQuantityItem () {
        return quantityItem;
    }

    /**
     * Setter mengden for varen
     * @param quantityItem mengden av varen
     * @throws IllegalArgumentException hvis man skriver inn et negativt tall
     */
    public void setQuantityItem (double quantityItem) {
        if(quantityItem > 0) {
            this.quantityItem = quantityItem;
        } else {
            throw new IllegalArgumentException("Quanity cannot be negative");
        }
    }

    /**
     * Henter enhet for varen
     * @return enhet av varen
     */
    public String getUnitItem() {
        return unitItem;
    }

    /**
     * Setter enhet for varen
     * @param unitItem enhet av varen
     */
    public void setUnitItem(String unitItem) {
        this.unitItem = unitItem;
    }

    /**
     * Henter pris per enhet for varen
     * @return pris per enhet for varen
     */
    public double getPricePerUnit() {
        return pricePerUnit;
    }

    /**
     * Skriver inn pris per enhet
     * @param pricePerUnit pris per enhet
     * @throws IllegalArgumentException dersom man skriver inn negativt tall
     */
    public void setPricePerUnit(double pricePerUnit) {
        if(pricePerUnit > 0) {
            this.pricePerUnit = pricePerUnit;
        } else {
            throw new IllegalArgumentException("Price per unit cannot be negative");
        }
    }

    /**
     * Henter best-før-dato av varen
     * @return best-før-dato
     */
    public LocalDate getBestBefore() {
        return bestBefore;
    }

    /**
     * Setter best-før-dato til varen
     * @param bestBefore best-før-dato
     */
    public void setBestBefore(LocalDate bestBefore) {
        this.bestBefore = bestBefore;
    }

    //
    @Override
    public String toString() {
        return "Name of item: " + nameItem + ", quantity: " + quantityItem + " " + unitItem + "\n" +
                "Price per unit: " + pricePerUnit + " kr," + " best-before-date: " + bestBefore + "\n";
    }
}