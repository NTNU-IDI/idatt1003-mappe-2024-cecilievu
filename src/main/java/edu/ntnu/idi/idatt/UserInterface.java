package edu.ntnu.idi.idatt;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*; //importing all utility classes

/**
 * The UserInterFace class handles the interaction with the user.
 * It provides a menu for the user to manage the fridge such as
 * adding, removing, searching or displaying items.
 */
public class UserInterface {
    private final Scanner scanner = new Scanner(System.in);
    private final FoodStorage foodStorage = new FoodStorage();
    private final CookBook cookBook = new CookBook();
    private final DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    /**
     * Enum menu, representing options for the user.
     */
    private enum MenuOption {
        SHOW_ITEMS(1, "Show items in the fridge"),
        ADD_ITEM(2, "Add new item"),
        REMOVE_ITEM(3, "Remove item"),
        SEARCH_ITEM(4, "Search for an item"),
        SHOW_ITEM_BY_DATE(5, "Show list of items by date"),
        SHOW_EXPIRED(6, "Show expired items"),
        SHOW_TOTAL_VALUE(7, "Show total value in the fridge"),
        SHOW_ALL_RECIPES(8, "Show all recipes"),
        ADD_NEW_RECIPE(9, "Add new recipe"),
        REMOVE_RECIPE(10, "Remove recipe"),
        CHECK_RECIPE(11, "Check if a recipe can be made"),
        SUGGEST_RECIPE(12, "Suggest recipes from items in fridge"),
        EXIT(13, "End program");

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

        /**
         * Finds a menu option based on its value.
         *
         * @param value the "int" value of the menu options
         * @return an Optional, containing the chosen value from MenuOption
         */
        public static Optional<MenuOption> fromValue(int value) {
            return Arrays.stream(values()).filter(option -> option.value == value).findFirst();
        }
    }

    /**
     * Initialize the food storage/fridge with predefined items.
     */
    public void init() {
        // Forhåndsdefinerte varer
        foodStorage.addItem(new Ingredient("Egg", 12, "pieces", 2.0, LocalDate.of(2024, 12, 24)));
        foodStorage.addItem(new Ingredient( "Milk", 3, "L", 10.0, LocalDate.of(2024,12,24)));
        foodStorage.addItem(new Ingredient("Butter", 250, "grams", 0.1, LocalDate.of(2024,12,24)));
        foodStorage.addItem(new Ingredient("Flour", 1000, "grams",0.03, LocalDate.of(2024,12,24)));
    }

    /**
     * Starts the application, representing the menu and handling user input.
     */
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
                actions.get(menuOption.get()).run(); // Runs the chosen action
            } else {
                System.out.println("Invalid option, please try again");
            }
        }
    }

    // Kartlegging av manyvalg til metoder

    /**
     * Maps meny options to their corresponding methods.
     *
     * @return a mop of MenuOptions to Runnable actions
     */
    private Map<MenuOption, Runnable> createActions() {
        Map<MenuOption, Runnable> actions = new HashMap<>();
        actions.put(MenuOption.SHOW_ITEMS, this::handleShowItem);
        actions.put(MenuOption.ADD_ITEM, this::handleAddItem);
        actions.put(MenuOption.REMOVE_ITEM, this::handleRemoveItem);
        actions.put(MenuOption.SEARCH_ITEM, this::handleSearchItem);
        actions.put(MenuOption.SHOW_ITEM_BY_DATE, this::handleShowItemByDate);
        actions.put(MenuOption.SHOW_EXPIRED, this::handleShowExpiredItems);
        actions.put(MenuOption.SHOW_TOTAL_VALUE, this::handleShowTotalValue);
        actions.put(MenuOption.SHOW_ALL_RECIPES, this::handleShowRecipe);
        actions.put(MenuOption.ADD_NEW_RECIPE, this::handleAddRecipe);
        actions.put(MenuOption.CHECK_RECIPE, this::handleCheckRecipe);
        actions.put(MenuOption.SUGGEST_RECIPE, this::handleSuggestRecipe);
        return actions;
    }

    /**
     * Prints menu display for the user.
     */
    private void printMenu() {
        System.out.println("============= Menu =============");
        for (MenuOption option : MenuOption.values()) {
            System.out.printf("%d. %s%n", option.getValue(), option.getDescription()); //
        }
    }

    /**
     * Displays items in the fridge, sorted by name and best-before date.
     */
    private void handleShowItem() {
        List<Ingredient> items = foodStorage.getItems();
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

    /**
     *
     * Prompts the user to add new item to the fridge.
     */
    private void handleAddItem() {
        String name = readString("Type in name of the new item: ");
        double quantity = readDouble("Type quantity of item: ");
        String unit = readString("Type unit of measurement (L, dL, kg, grams or pieces): ");
        double price = readDouble("Price per unit: ");
        LocalDate bestBefore = readDate("Type in best before date (dd-MM-yyyy): ");

        String message = foodStorage.addItem(new Ingredient(name, quantity, unit, price, bestBefore));
        System.out.println(message);
    }

    /**
     * Prompts the user to remove item from the fridge.
     */
    private void handleRemoveItem() {
        String name = readString("Type in the item you want to remove: ");
        double quantity = readDouble("Type in the quantity you want to remove: ");

        String message = foodStorage.removeItem(name, quantity);
        System.out.println(message);
    }

    /**
     * Prompts the user to search for a specific item by name.
     * Displays the matching item sorted by name and date.
     */
    private void handleSearchItem() {
        String name = readString("Type in item name: ");
        List<Ingredient> matchingItems = foodStorage.searchItem(name);

        if (matchingItems.isEmpty()) {
            System.out.println("No matching item with the name: " + name);
        } else {
            System.out.println("Items found with the name: " + name);
            printListItem();
            matchingItems.stream()
                    .sorted(Comparator.comparing(Ingredient::getBestBefore))
                    .forEach(item -> {
                        String formatted = String.format("%-8s      | %7.2f   %-7s | %6.2f kr      | %4s",
                                item.getNameItem(), item.getQuantityItem(), item.getUnitItem(), item.getPricePerUnit(), item.getBestBefore()
                        );
                        System.out.println(formatted);
                    });
            System.out.println();
        }
    }

    /**
     * Prompts the user to search items by best-before date.
     */
    private void handleShowItemByDate() {
        LocalDate date = readDate("Enter a date (dd-MM-yyyy): ");
                List<Ingredient> itemByDate = foodStorage.searchItemByDate(date);

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
            System.out.println();
        }
    }

    /**
     * Displays expired items and its total value.
     * Encourages the user to check items before throwing out to reduce food waste.
     */
    private void handleShowExpiredItems() {
        List<Ingredient> expiredItems = foodStorage.getExpiredItems();

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
            System.out.println();
        }
    }

    /**
     * Displays total value of all the items in the fridge.
     * Each item's value is calculated as quantity * pricer per unit.
     */
    private void handleShowTotalValue() {
        double totalValue = foodStorage.calculateTotalValue();
        System.out.printf("Total value of items: %.2f kr%n", totalValue);
        System.out.println();
    }

    // COOKBOOK
    public void handleShowRecipe() {

    }

    public void handleAddRecipe() {
        System.out.println("Step 1. Writing the recipe:");
        String name = readString("Type in name of the new recipe: ");
        String description = readString("Type in a short description of the recipe: ");
        String instruction = readString("Type in introduction for recipe: ");
        int servings = readInt("Type in the number of servings: ");
        System.out.println();

        List<Ingredient> ingredients = new ArrayList<>();
        boolean addMoreIngredients = true;

        while (addMoreIngredients) {
            System.out.println("Step 2. Adding ingredients with its quantity and unit for the recipe:");
            String ingredientName = readString("Type in an ingredient: ");
            double ingredientQuantity = readDouble("Type in quantity: ");
            String ingredientUnit = readString("Type in unit (e.g dl, grams or pieces): ");

            ingredients.add(new Ingredient(ingredientName, ingredientQuantity, ingredientUnit, 0.0, LocalDate.MAX));
            addMoreIngredients = readString("Do you want to add another ingredient? (yes/no): ")
                    .equalsIgnoreCase("yes");
        }
        Recipe recipe = new Recipe(name, description, instruction, servings);
        String result = cookBook.addRecipe(recipe);
        System.out.println(result);
    }

    public void handleCheckRecipe() {
        String nameRecipe = readString("Type in recipe name: ");
    }

    public void handleSuggestRecipe() {

    }

    /**
     * Reads a non-empty "string" input from user.
     * Continues to prompt until a valid input is entered.
     *
     * @param prompt the message displayed to the user to guide input
     * @return the user's input as a non-empty string
     */
    private String readString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (!input.isBlank() && input.matches("[a-zA-ZÆØÅæøå\\s]+")) {
                return input;
            }
            System.out.println("Invalid input, please try again. ");
        }
    }

    /**
     * Reads an "int" input from user.
     * Continues to prompt until valid input is entered.
     *
     * @param prompt the message displayed to the user to guide input
     * @return the user's input as an int
     */
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

    /**
     * Reads a positive "double" value from user.
     * Ensure that the value is greater than zero.
     * Continues to prompt until valid input is entered.
     *
     * @param prompt the message displayed to the user to guide input
     * @return the user's input as a positive double
     */
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

    /**
     * Reads a date input from user in the format "dd-MM-yyyy"
     * Continues to prompt until valid input is entered.
     *
     * @param prompt the message displayed to the user to guide input
     * @return the user's input as a LocalDate
     */
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

    /**
     * Prints the table header for displaying items in fridge.
     * This includes columns for the item name, quantity, unit, price per unit and best-before date.
     * The method is to ensure consistent formatting across different output
     * such as showing all items, search for items or show expired items.
     */
    private void printListItem() {
        System.out.println();
        System.out.println("Name          | Quantity  Unit    | Price per unit | Best before date   ");
        System.out.println("---------------------------------------------------------------------");
    }
}
