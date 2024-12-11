package edu.ntnu.idi.idatt.views;

import edu.ntnu.idi.idatt.models.CookBook;
import edu.ntnu.idi.idatt.models.FoodStorage;
import edu.ntnu.idi.idatt.models.Ingredient;
import edu.ntnu.idi.idatt.models.Recipe;
import edu.ntnu.idi.idatt.models.TestData;
import edu.ntnu.idi.idatt.utils.Utils;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * The UserInterFace class handles the interaction with the user for the "Fridge and CookBook"
 * application. It provides a menu for the user to manage the fridge or cookbook such as adding,
 * removing, searching or displaying items/recipes.
 */
public class UserInterface {

  private final FoodStorage foodStorage = new FoodStorage();
  private final CookBook cookBook = new CookBook();
  private final Utils utils = new Utils();

  /**
   * Enum menu, representing options for the user from 1-14.
   */
  public enum MenuOption {
    SHOW_ITEMS(1, "Show items in the fridge"),
    ADD_ITEM(2, "Add new item"),
    REMOVE_ITEM(3, "Remove item"),
    SEARCH_ITEM(4, "Search for an item"),
    SHOW_ITEM_BY_DATE(5, "Show list of items by date"),
    SHOW_EXPIRED(6, "Show expired items"),
    SHOW_TOTAL_VALUE(7, "Show total value in the fridge"),
    SHOW_ALL_RECIPES(8, "Show all recipes"),
    EXPAND_RECIPE(9, "Expand a recipe"),
    ADD_NEW_RECIPE(10, "Add new recipe"),
    REMOVE_RECIPE(11, "Remove recipe"),
    CHECK_RECIPE(12, "Check if a recipe can be made from items in fridge"),
    SUGGEST_RECIPE(13, "Suggest recipes from items in fridge"),
    EXIT(14, "End program");

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
    TestData.getPreDefinedItems().forEach(foodStorage::addItem);

    // Forhåndsdefinterte oppskrifter
    TestData.getPreDefineRecipes().forEach(cookBook::addRecipe);
  }

  /**
   * Starts the application, representing the menu and handling users input.
   */
  public void start() {
    Map<MenuOption, Runnable> actions = createActions(); // Kartlegging av menyvalg til funksjoner

    while (true) {
      Utils.printMenu(MenuOption.values()); // Kaller på printMenu metoden fra utils.
      int choice = utils.readInt("Choose an option: ");
      Optional<MenuOption> menuOption = MenuOption.fromValue(choice);

      // Ved avslutning (valg nummer 14), brytes løkken ut.
      if (menuOption.isPresent()) {
        if (menuOption.get() == MenuOption.EXIT) {
          System.out.println();
          System.out.println(
              "Thank you for using the Fridge and Cookbook Manager. See you next time! :)");
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
   * Maps menu options to their corresponding methods.
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
    actions.put(MenuOption.EXPAND_RECIPE, this::handleExpandRecipe);
    actions.put(MenuOption.ADD_NEW_RECIPE, this::handleAddRecipe);
    actions.put(MenuOption.REMOVE_RECIPE, this::handleRemoveRecipe);
    actions.put(MenuOption.CHECK_RECIPE, this::handleCheckRecipe);
    actions.put(MenuOption.SUGGEST_RECIPE, this::handleSuggestRecipe);
    return actions;
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
      utils.printListItem();
      items.stream()
          .sorted(Comparator.comparing((Ingredient item) -> item.getNameItem().toLowerCase())
              .thenComparing(Ingredient::getBestBefore))
          .forEach(item -> {
            String formatted = String.format("%-12s | %7.2f   %-6s | %6.2f  kr     | %4s",
                item.getNameItem(), item.getQuantityItem(), item.getUnitItem(),
                item.getPricePerUnit(), item.getBestBefore()
            );
            System.out.println(formatted);
          });
      System.out.println();
    }
  }

  /**
   * Prompts the user to add new item to the fridge.
   */
  private void handleAddItem() {
    try {
      String name = utils.readString("Type in name of the new item: ");
      double quantity = utils.readDouble("Type quantity of item: ");
      String unit = utils.readString("Type unit of measurement (e.g dL, grams or pcs): ");
      double price = utils.readDouble("Price per unit: ");
      LocalDate bestBefore = utils.readDate("Type in best before date (dd-MM-yyyy): ");

      String message = foodStorage.addItem(new Ingredient(name, quantity, unit, price, bestBefore));
      System.out.println();
      System.out.println(message);
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
    }

  }

  /**
   * Prompts the user to remove item from the fridge.
   */
  private void handleRemoveItem() {
    try {
      String name = utils.readString("Type in the item you want to remove: ");
      double quantity = utils.readDouble("Type in the quantity you want to remove: ");

      String message = foodStorage.removeItem(name, quantity);
      System.out.println();
      System.out.println(message);
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
    }
  }

  /**
   * Prompts the user to search for a specific item by name. Displays the matching item sorted by
   * name and date.
   */
  private void handleSearchItem() {
    String name = utils.readString("Type in item name: ");
    List<Ingredient> matchingItems = foodStorage.searchItem(name);

    if (matchingItems.isEmpty()) {
      System.out.println("No matching item with the name: " + name);
    } else {
      System.out.println("Items found with the name: " + name);
      utils.printListItem();
      matchingItems.stream()
          .sorted(Comparator.comparing(Ingredient::getBestBefore))
          .forEach(item -> {
            String formatted = String.format("%-12s | %7.2f   %-6s | %6.2f  kr     | %4s",
                item.getNameItem(), item.getQuantityItem(), item.getUnitItem(),
                item.getPricePerUnit(), item.getBestBefore()
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
    LocalDate date = utils.readDate("Enter a date (dd-MM-yyyy): ");
    List<Ingredient> itemByDate = foodStorage.getItemsBeforeDate(date);

    if (itemByDate.isEmpty()) {
      System.out.println("No item found with the best-before-date " + date);
    } else {
      System.out.println("Items with the best-before-date " + date + ":");
      utils.printListItem();
      itemByDate.forEach(item -> {
        String formatted = String.format("%-12s | %7.2f   %-6s | %6.2f  kr     | %4s",
            item.getNameItem(), item.getQuantityItem(), item.getUnitItem(), item.getPricePerUnit(),
            item.getBestBefore());
        System.out.println(formatted);
      });
      System.out.println();
    }
  }

  /**
   * Displays expired items and its total value. Encourages the user to check items before throwing
   * out to reduce food waste.
   */
  private void handleShowExpiredItems() {
    List<Ingredient> expiredItems = foodStorage.getExpiredItems();

    if (expiredItems.isEmpty()) {
      System.out.println();
      System.out.println("No items have expired!\n");
    } else {
      System.out.println();
      System.out.println("Expired items:");
      utils.printListItem();
      expiredItems.forEach(item -> {
        String formatted = String.format("%-12s | %7.2f   %-6s | %6.2f  kr     | %4s",
            item.getNameItem(), item.getQuantityItem(), item.getUnitItem(), item.getPricePerUnit(),
            item.getBestBefore());
        System.out.println(formatted);
      });

      double totalValue = expiredItems.stream()
          .mapToDouble(item -> item.getQuantityItem() * item.getPricePerUnit())
          .sum();
      System.out.println("Total value of expired items: " + totalValue + " kr");
      System.out.println(
          "Before throwing out, LOOK - SMELL - TASTE! Trust your senses, reduce foodwaste! :)");
      System.out.println();
    }
  }

  /**
   * Displays total value of all the items in the fridge. Each item's value is calculated as
   * quantity * pricer per unit.
   */
  private void handleShowTotalValue() {
    double totalValue = foodStorage.calculateTotalValue();
    System.out.println();
    System.out.printf("Total value of items: %.2f kr", totalValue);
    System.out.println();
  }

  // COOKBOOK
  public void handleShowRecipe() {
    List<Recipe> recipes = cookBook.getRecipes();
    if (recipes.isEmpty()) {
      System.out.println("There's no recipes in the cookbook.");
    } else {
      utils.printListRecipes();

      recipes.stream()
          .sorted(Comparator.comparing(recipe -> recipe.getNameRecipe().toLowerCase()))
          .forEach(recipe -> {
            String formatted = String.format("%-18s | %-50s | %d",
                recipe.getNameRecipe(), recipe.getDescriptionRecipe(), recipe.getServingsRecipe()
            );
            System.out.println(formatted);
          });
      System.out.println();
    }
  }

  public void handleExpandRecipe() {
    try {
      String nameRecipe = utils.readString("Type in recipe name: ");
      String message = cookBook.expandRecipe(nameRecipe);
      System.out.println();
      System.out.println(message);
      System.out.println("Enjoy! :)");
      System.out.println();
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
    }
  }

  public void handleAddRecipe() {
    try {
      System.out.println();
      System.out.println("Step 1. Writing the recipe");
      System.out.println("----------------------------");
      String name = utils.readString("Type in name of the new recipe: ");
      String description = utils.readString("Type in a short description of the recipe: ");
      String instruction = utils.readString("Type in instructions for recipe: ");
      int servings = utils.readInt("Type in the number of servings: ");
      System.out.println("Step 2. Adding ingredients with its quantity and unit for the recipe:");
      System.out.println("----------------------------------------------------------------------");

      List<Ingredient> ingredients = new ArrayList<>();
      boolean addMoreIngredients = true;

      while (addMoreIngredients) {
        String ingredientName = utils.readString("Type in an ingredient: ");
        double ingredientQuantity = utils.readDouble("Type in quantity: ");
        String ingredientUnit = utils.readString("Type in unit (e.g dl, grams or pcs): ");
        ingredients.add(
            new Ingredient(ingredientName, ingredientQuantity, ingredientUnit, 0.0, LocalDate.MAX));
        addMoreIngredients = utils.readString("Do you want to add another ingredient? (yes/no): ")
            .equalsIgnoreCase("yes");
      }
      Recipe recipe = new Recipe(name, description, instruction, ingredients, servings);
      String result = cookBook.addRecipe(recipe);
      System.out.println(result);
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
    }

  }

  public void handleRemoveRecipe() {
    try {
      String name = utils.readString("Type in name of the recipe to remove: ");
      String result = cookBook.removeRecipe(name);
      System.out.println();
      System.out.println(result);
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
    }
  }

  public void handleCheckRecipe() {
    try {
      String recipeName = utils.readString("Type in recipe name: ");
      String result = cookBook.canMakeRecipe(recipeName, foodStorage);
      System.out.println();
      System.out.println(result);
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
    }


  }

  public void handleSuggestRecipe() {
    List<String> suggestions = cookBook.suggestRecipe(foodStorage);
    System.out.println();
    if (suggestions.isEmpty()) {
      System.out.println("No recipe can be made with the current items in the fridge");
      System.out.println();
    } else {
      System.out.println("You can make the following recipes from items in the fridge: ");
      suggestions.forEach(recipe -> System.out.println("- " + recipe));
      System.out.println();
    }
  }
}
