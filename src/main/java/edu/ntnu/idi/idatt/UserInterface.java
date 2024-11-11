package edu.ntnu.idi.idatt;

import java.util.Optional; //klasse som henter tidligere verdier
import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter; //klasse som brukes til å formatere og parse dato i java. Lar den definere dato som dd-MM-yyyy
import java.time.format.DateTimeParseException; //unntaksklasse som brukes til å indikere om det oppstår feil under parsing av dato, eks du skriver MM-dd-yyyy

//Variabler som skal initaliserers inn i init()-metoden
public class UserInterface {
    private Scanner scanner;
    private FoodStorage foodStorage;
    private DateTimeFormatter dateTimeFormat;

    /**
     * Metoden init() initialiserer nødvendige komponenter før programmet stater
     * - her oppretter den scanner til bruker
     * - initaliserer et foodStorage objekt
     * - setter opp en DateTimeFormatter for å håndtere datoformatering
     */
    public void init() {
        scanner = new Scanner(System.in);
        foodStorage = new FoodStorage();
        dateTimeFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    }

    /**
     * Metoden start() håndterer "hoveddelen" for brukerinteraksjon
     */
    public void start() {
        while(true) {
            System.out.println("Menu1");
            System.out.println("1. Show items in fridge");
            System.out.println("2. Add new item");
            System.out.println("3. Remove item");
            System.out.println("4. Search for item");
            System.out.println("5. Show expired items");
            System.out.println("6. Show total value of items in fridge");
            System.out.println("7. End program");
            int valg = scanner.nextInt();
            scanner.nextLine();

            switch(valg) {
                case 1:
                    foodStorage.sortItemByNameAndDate();
                    foodStorage.showItem();
                    break;

                case 2:
                    System.out.println("Type name of the new item: ");
                    String nameItem = scanner.nextLine();

                    Optional<Ingredient> exsistingItem = foodStorage.findLatestItem(nameItem);
                    if(exsistingItem.isPresent()) {
                        Ingredient item = exsistingItem.get();
                        System.out.println("Previous entry found for " + nameItem + ":");
                        System.out.println("Best before date: " + exsistingItem.get().getBestBefore().format(dateTimeFormat));
                        System.out.println("Price per unit: " + exsistingItem.get().getPricePerUnit());
                    }

                    System.out.println("Type quantity of item: ");
                    double quantityItem = scanner.nextDouble();
                    scanner.nextLine();

                    System.out.println("Type the unit of measurement ('L', 'dL', 'mL', 'grams', 'kg' or 'pieces' i.e for eggs/carrots etc.))");
                    String unitItem = scanner.nextLine();

                    System.out.println("Price per unit for item: ");
                    double pricePerUnit = scanner.nextDouble();
                    scanner.nextLine();

                    System.out.println("Type best before date for item (dd-MM-yyyy): ");
                    String dateString = scanner.nextLine();

                    LocalDate bestBefore = null;
                    try {
                        bestBefore = LocalDate.parse(dateString, dateTimeFormat);
                    } catch(DateTimeParseException e) {
                        System.out.println("Invalid date format. Please use dd-MM-yyyy");
                        break;
                    }

                    Ingredient newItem = new Ingredient(nameItem, quantityItem, unitItem, pricePerUnit, bestBefore);
                    foodStorage.addItem(newItem);
                    break;

                case 3:
                    System.out.println("Which item do you want to remove from the fridge?");
                    String nameToRemove = scanner.nextLine();
                    System.out.println("Write down quantity of the item you want to remove: ");
                    double quantityToRemove = scanner.nextDouble();
                    foodStorage.removeItem(nameToRemove, quantityToRemove);
                    break;

                case 4:
                    System.out.println("Write item: ");
                    String nameToFind = scanner.nextLine();
                    Ingredient foundItem = foodStorage.searchItem(nameToFind);
                    if(foundItem == null) {
                        System.out.println(nameToFind + " does not exist in the fridge");
                    }
                    break;

                case 5:
                    foodStorage.showExpiredItems();
                    break;

                case 6:
                    double totalValue = foodStorage.calculateTotalValue();
                    System.out.println("Total value of items in the fridge: " + totalValue + " kr");
                    break;

                case 7:
                    System.out.println("Ending program...");
                    return;
                default:
                    System.out.println("Invalid choice");
            }
        }
    }
}
