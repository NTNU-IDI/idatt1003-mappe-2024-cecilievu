package edu.ntnu.idi.idatt;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

//Variabler som skal initaliserers inn i init()-metoden
public class UserInterface {
    private final Scanner scanner = new Scanner(System.in);
    private final FoodStorage foodStorage = new FoodStorage();
    private final DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    private enum MenuOption {
        SHOW_ITEMS(1, "Show items in the fridge"),
        ADD_ITEM(2, "Add new item"),
        REMOVE_ITEM(3, "Remove item"),
        SEARCH_ITEM(4, "Search for an item"),
        SHOW_ITEM_BY_DATE(5, "Show list of items by date"),
        SHOW_EXPIRED(6, "Show expired items"),
        SHOW_TOTAL_VALUE(7, "Show total value in the fridge"),
        EXIT(8, "End program");

        private final int value;
        private final String description;

        MenuOption(int value, String description) {
            this.value = value;
            this.description = description;
        }

        public int getValue() {
            return value;
        }

        public String getDescription() {
            return description;
        }

        public static Optional<MenuOption> fromValue(int value) {
            return Arrays.stream(values()).filter(option -> option.value == value).findFirst();
        }
    }

    public void init() {
        // Forhåndsdefinerte varer
        foodStorage.addItem(new Ingredient("Egg", 12, "pieces", 2.0, LocalDate.of(2024, 12, 24)));
        foodStorage.addItem(new Ingredient( "Milk", 3, "L", 10.0, LocalDate.of(2024,12,24)));
        foodStorage.addItem(new Ingredient("Butter", 250, "grams", 0.1, LocalDate.of(2024,12,24)));
        foodStorage.addItem(new Ingredient("Flour", 1000, "grams",0.03, LocalDate.of(2024,12,24)));
    }

    public void start() {
        Map<MenuOption, Runnable> actions = createActions(); // Kartlegging av menyvalg til funksjoner

        while(true) {
            printMenu();
            int choice = readInt("Choose an option: ");
            Optional<MenuOption> menuOption = MenuOption.fromValue(choice);

            if (menuOption.isPresent()) {
                if (menuOption.get() == MenuOption.EXIT) {
                    System.out.println("Ending program... Goodbye!");
                    break;
                }
                actions.get(menuOption.get()).run(); // Kjør valgt handling
            } else {
                System.out.println("Invalid option, please try again");
            }
        }
    }

    // Kartlegging av manyvalg til metoder
    private Map<MenuOption, Runnable> createActions() {
        Map<MenuOption, Runnable> actions = new HashMap<>();
        actions.put(MenuOption.SHOW_ITEMS, this::handleShowItem);
        actions.put(MenuOption.ADD_ITEM, this::handleAddItem);
        actions.put(MenuOption.REMOVE_ITEM, this::handleRemoveItem);
        actions.put(MenuOption.SEARCH_ITEM, this::handleSearchItem);
        actions.put(MenuOption.SHOW_ITEM_BY_DATE, this::handleShowItemByDate);
        actions.put(MenuOption.SHOW_EXPIRED, this::handleShowExpiredItems);
        actions.put(MenuOption.SHOW_TOTAL_VALUE, this::handleShowTotalValue);
        return actions;
    }

    // Menyvisning
    private void printMenu() {
        System.out.println("============= Menu =============");
        for (MenuOption option : MenuOption.values()) {
            System.out.printf("%d. %s%n", option.getValue(), option.getDescription()); //
        }
    }

    // Vise alle varene
    private void handleShowItem() {
        ArrayList<Ingredient> items = foodStorage.getItems();
        if (items.isEmpty()) {
            System.out.println("The fridge is empty");
        } else {
            System.out.println("Items in the fridge:");
            printListItem();
            items.stream()
                    .sorted(Comparator.comparing((Ingredient item) -> item.getNameItem().toLowerCase())
                            .thenComparing(Ingredient::getBestBefore))
                    .forEach(item -> {
                        String formatted = String.format("%-8s      | %7.2f   %-7s | %6.2f kr      | %4s",
                                item.getNameItem(), item.getQuantityItem(), item.getUnitItem(), item.getPricePerUnit(), item.getBestBefore()
                        );
                        System.out.println(formatted);
                    });
            System.out.println();
        }
    }

    // Legge til vare
    private void handleAddItem() {
        String name = readString("Type in name of the new item: ");
        double quantity = readDouble("Type quantity of item: ");
        String unit = readString("Type unit of measurement (L, dL, kg, grams or pieces): ");
        double price = readDouble("Price per unit: ");
        LocalDate bestBefore = readDate("Type in best before date (dd-MM-yyyy): ");

        foodStorage.addItem(new Ingredient(name, quantity, unit, price, bestBefore));
    }

    // Fjerner varer
    private void handleRemoveItem() {
        String name = readString("Type in the item you want to remove: ");
        double quantity = readDouble("Type in the quantity you want to remove: ");
        foodStorage.removeItem(name, quantity);
    }

    // Søke etter varer
    private void handleSearchItem() {
        String name = readString("Type in item name: ");
        ArrayList<Ingredient> matchingItems = foodStorage.searchItem(name);

        if (matchingItems.isEmpty()) {
            System.out.println("No matching item with the name: " + name);
        } else {
            System.out.println("Items found with the name: " + name);
            printListItem();
            matchingItems.stream()
                    .sorted(Comparator.comparing(Ingredient::getNameItem))
                    .forEach(item -> {
                        String formatted = String.format("%-8s      | %7.2f   %-7s | %6.2f kr      | %4s",
                                item.getNameItem(), item.getQuantityItem(), item.getUnitItem(), item.getPricePerUnit(), item.getBestBefore()
                        );
                        System.out.println(formatted);
                    });
        }
    }

    // Søke varer etter dato
    private void handleShowItemByDate() {
        LocalDate date = readDate("Enter a date (dd-MM-yyyy): ");
                ArrayList<Ingredient> itemByDate = foodStorage.searchItemByDate(date);

        if (itemByDate.isEmpty()) {
            System.out.println("No item found with the best-before-date " + date);
        } else {
            System.out.println("Items with the best-before-date " + date + ":");
            printListItem();
            itemByDate.forEach(item -> {
                String formatted = String.format("%-8s      | %7.2f   %-7s | %6.2f kr      | %4s",
                        item.getNameItem(), item.getQuantityItem(), item.getUnitItem(), item.getPricePerUnit(), item.getBestBefore());
                System.out.println(formatted);
            });
        }
    }

    // Vise varer utgått på dato + total value
    private void handleShowExpiredItems() {
        ArrayList<Ingredient> expiredItems = foodStorage.getExpiredItems();

        if (expiredItems.isEmpty()) {
            System.out.println("No items have expired!");
        } else {
            System.out.println("Expired items:");
            printListItem();
            expiredItems.forEach(item -> {
                String formatted = String.format("%-8s | %7.2f   %-7s | %6.2f kr      | %4s",
                        item.getNameItem(), item.getQuantityItem(), item.getUnitItem(), item.getPricePerUnit(), item.getBestBefore() );
                System.out.println(formatted);
            });

            double totalValue = expiredItems.stream()
                    .mapToDouble(item -> item.getQuantityItem() * item.getPricePerUnit())
                    .sum();
            System.out.println("Total value of expired items: " + totalValue + " kr");
            System.out.println("Before throwing out, LOOK - SMELL - TASTE! Trust your senses, reduce foodwaste! :)");
        }
    }

    // Vise totalverdi
    private void handleShowTotalValue() {
        double totalValue = foodStorage.calculateTotalValue();
        System.out.printf("Total value of items: %.2f kr%n", totalValue);
    }

    // Inputmetoder!
    private String readString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            if (!input.isBlank()) {
                return input;
            }
            System.out.println("Invalid input, please try again. ");
        }
    }

    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Invalid input, please enter a number. ");
            }
        }
    }

    private double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                double value = Double.parseDouble(scanner.nextLine().trim());
                if (value > 0) {
                    return value;
                }
                System.out.print("Invalid input, quantity cannot be negative. Please try again. ");
            } catch (NumberFormatException e) {
                System.out.print("Invalid input, please enter a number. ");
            }
        }
    }


    private LocalDate readDate(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return LocalDate.parse(scanner.nextLine(), dateTimeFormat);
            } catch (DateTimeParseException e) {
                System.out.print("Invalid date format, please use dd-MM-yyy.");
            }
        }
    }

    private void printListItem() {
        System.out.println();
        System.out.println("Name          | Quantity  Unit    | Price per unit | Best before date   ");
        System.out.println("---------------------------------------------------------------------");
    }
}
