package edu.ntnu.idi.idatt.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class Utils {
    private final Scanner scanner = new Scanner(System.in);
    private final DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    /**
     * Reads a non-empty "string" input from user.
     * Continues to prompt until a valid input is entered.
     *
     * @param prompt the message displayed to the user to guide input
     * @return the user's input as a non-empty string
     */
    public String readString(String prompt) {
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
    public int readInt(String prompt) {
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
    public double readDouble(String prompt) {
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
    public LocalDate readDate(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return LocalDate.parse(scanner.nextLine(), dateTimeFormat);
            } catch (DateTimeParseException e) {
                System.out.print("Invalid date format, please use dd-MM-yyyy.");
            }
        }
    }

    /**
     * Prints the table header for displaying items in fridge.
     */
    public void printListItem() {
        System.out.println();
        System.out.println("Name         | Quantity  Unit   | Price per unit | Best before date   ");
        System.out.println("--------------------------------------------------------------------");
    }

    public void printListRecipes() {
        System.out.println("Recipes in the cookbook: ");
        System.out.println();
        System.out.println("Name               | Description                                        | Servings");
        System.out.println("--------------------------------------------------------------------------------");
    }
}
