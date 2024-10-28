package edu.ntnu.idi.idatt;

import java.util.ArrayList;
import java.util.Optional;
import java.time.LocalDate; //nyerer versjon enn util.date

public class FoodStorage {
    //attributes med array list for varer i kjøleskap
    private ArrayList<CreateItem> items;

    /**
     * Konstruktør som skal ta inn alle varene i en liste
     */
    public FoodStorage() {
        this.items = new ArrayList<>();
    }

    /**
     * Metode for å legge til ny vare
     * Legger også til at dersom du ønsker å legge til en eksisterende vare så sjekker den om den har lik
     * dato + pris, hvis ikke blir det lagt til som ny vare
     * @param newItem ny vare
     */
    public void addItem(CreateItem newItem) {
        boolean itemExsist = false;
        for(CreateItem item : items) {
            if(item.getNameItem().equalsIgnoreCase(newItem.getNameItem()) && item.getBestBefore().equals(newItem.getBestBefore()) && item.getPricePerUnit() == newItem.getPricePerUnit()) {
                item.setQuantityItem(item.getQuantityItem() + newItem.getQuantityItem());
                System.out.println(newItem.getQuantityItem() + " " + newItem.getUnitItem() + " of " + newItem.getNameItem() + " has been added to exsisting item in the fridge.");
                itemExsist = true;
                break;
            }
        }
        if(!itemExsist) {
            items.add(newItem);
            System.out.println(newItem.getNameItem() + " has been added to the fridge.");
        }
        //CreateItem exsistingItem = searchItem(newItem.getNameItem());
        //if (exsistingItem != null) {
        //exsistingItem.setQuantityItem(exsistingItem.getQuantityItem() + newItem.getQuantityItem());
        //System.out.println(newItem.getQuantityItem() + " has been added to the exsisting item in the fridge.");
        //} else {
        //items.add(newItem);
        //System.out.println(newItem.getNameItem() + " is added to the fridge");
        //}
    }

    /**
     * Metode for å vise varene i kjøleskapet
     */
    public void showItem() {
        if(items.isEmpty()) {
            System.out.println("The fridge is empty");
        } else {
            System.out.println("Items in the fridge:");
            for (CreateItem item : items) {
                System.out.println(item);
            }
        }
    }

    /**
     * Metode for å søke etter en spesifikk vare
     * @param name navnet på varen man ønsker å finne
     * @return returnerer vare dersom den finnes, hvis ikke null
     */
    public CreateItem searchItem(String name) {
        for(CreateItem item : items) {
            if(item.getNameItem().equalsIgnoreCase(name)) { //equalsIgnoreCase gjør den case-insensitiv
                System.out.println("Item found in the fridge: " + item.getNameItem() + ", quantity of item: "
                        + item.getQuantityItem() + " " + item.getUnitItem());
                return item;
            }
        }
        //System.out.println("Item not found in fridge: " + name);
        return null;
    }

    /**
     * lager en optional-klass metode for å hente verdier som er blitt skrevet tidliger
     * den søker gjennom listen items etter elementer som har samme navn og returnerer det
     * @param name navnet på varen
     * @return returnerer et optional-objekt som enten inneholder en CreateItem eller er tom
     */
    public Optional<CreateItem> findLatestItem(String name) {
        return items.stream()
                .filter(item -> item.getNameItem().equalsIgnoreCase(name)) //lamda-uttrykk, obs sjekk ut mer om dette
                .findFirst();
    }

    /**
     * Metode for å fjerne en vare
     * @param name navnet på varen
     * @param quantity mengde av varen
     */
    public void removeItem(String name, double quantity) {
        CreateItem item = searchItem(name);
        if(item != null) {
            if(item.getQuantityItem() >= quantity) {
                item.setQuantityItem(item.getQuantityItem() - quantity);
                System.out.println(quantity + " " + item.getUnitItem() + " of " + name + " is removed from the fridge.");
            } else {
                System.out.println("Not enough quantity of " + name + " to remove");
            }
        } else {
            System.out.println(name + " does not exist in the fridge");
        }
    }

    /**
     * Metode for å vise varer som har gått ut av dato samt verdi av disse
     */
    public void showExpiredItems() {
        LocalDate today = LocalDate.now();
        double totalValue = 0; //lokale verdier
        boolean hasExpiredItems = false; //lokale verdier
        System.out.println("Expired items: ");
        for (CreateItem item : items) {
            if (item.getBestBefore().isBefore(today)) {
                System.out.println(item); //printer ut varene som er gått ut av dato
                totalValue += item.getQuantityItem() * item.getPricePerUnit(); //beregner verdien av varene som har gått ut av dato
                hasExpiredItems = true;
            }
        }
        if(!hasExpiredItems) {
            System.out.println("No items have expired! :)");
        } else {
            System.out.println("Total value of expired items: " + totalValue + " kr");
            System.out.println("Before throwing out, LOOK - SMELL - TASTE! Trust your senses, reduce foodwaste! :)");
        }
    }

    /**
     * Metode som beregner totalverdien av alle varene i kjøleskapet
     * @return totalverdi
     */
    public double calculateTotalValue() {
        double totalValue = 0;
        for (CreateItem item : items) {
            totalValue += item.getQuantityItem() * item.getPricePerUnit();
        }
        return totalValue;
    }
}
