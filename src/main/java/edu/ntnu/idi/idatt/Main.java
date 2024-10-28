package edu.ntnu.idi.idatt;

import java.util.Optional; //klasse som henter tidligere verdier
import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter; //klasse som brukes til å formatere og parse dato i java. Lar den definere dato som dd-MM-yyyy
import java.time.format.DateTimeParseException; //unntaksklasse som brukes til å indikere om det oppstår feil under parsing av dato, eks du skriver MM-dd-yyyy


public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        FoodStorage foodstorage = new FoodStorage();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");

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
                    foodstorage.showItem();
                    break;

                case 2:
                    System.out.println("Type name of the new item: ");
                    String nameItem = scanner.nextLine();

                    Optional<CreateItem> exsistingItem = foodstorage.findLatestItem(nameItem);
                    if(exsistingItem.isPresent()) {
                        CreateItem item = exsistingItem.get();
                        System.out.println("Previous entry found for " + nameItem + ":");
                        System.out.println("Best before date: " + exsistingItem.get().getBestBefore().format(dateFormat));
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
                        bestBefore = LocalDate.parse(dateString, dateFormat);
                    } catch(DateTimeParseException e) {
                        System.out.println("Invalid date format. Please use dd-MM-yyyy");
                        break;
                    }

                    CreateItem newItem = new CreateItem(nameItem, quantityItem, unitItem, pricePerUnit, bestBefore);
                    foodstorage.addItem(newItem);
                    break;

                case 3:
                    System.out.println("Which item do you want to remove from the fridge?");
                    scanner.nextLine();
                    String nameToRemove = scanner.nextLine();
                    System.out.println("Write down quantity of the item you want to remove: ");
                    double quantityToRemove = scanner.nextDouble();
                    scanner.nextLine();

                    foodstorage.removeItem(nameToRemove, quantityToRemove);
                    break;

                case 4:
                    System.out.println("Write item: ");
                    String nameToFind = scanner.nextLine();
                    CreateItem foundItem = foodstorage.searchItem(nameToFind);
                    if(foundItem == null) {
                        System.out.println(nameToFind + " does not exist in the fridge");
                    } else {
                        System.out.println(nameToFind + " exist in the fridge.");
                    }
                    break;

                case 5:
                    foodstorage.showExpiredItems();
                    break;

                case 6:
                    double totalValue = foodstorage.calculateTotalValue();
                    System.out.println("Total value of items in the fridge: " + totalValue + " kr");
                    break;

                case 7:
                    System.out.println("Ending program...");
                    return;
            }
        }

    }
}


