package edu.ntnu.idi.idatt.models;

import java.time.LocalDate;
import java.util.List;

public class TestData {
    public static List<Ingredient> getPreDefinedItems() {
        return List.of(
                new Ingredient("Egg", 12, "pcs", 2.0, LocalDate.of(2024, 12, 24)),
                new Ingredient( "Milk", 3, "dL", 10.0, LocalDate.of(2024,12,24)),
                new Ingredient("Butter", 250, "grams", 0.1, LocalDate.of(2024,12,15)),
                new Ingredient("Flour", 1000, "grams",0.03, LocalDate.of(2024,12,24)),
                new Ingredient("Apple", 5, "pcs", 7.0, LocalDate.of(2024, 12, 12)),
                new Ingredient("Baking soda", 250, "grams", 0.1, LocalDate.of(2024, 12,24)),
                new Ingredient("Sugar", 1000, "grams", 0.02, LocalDate.of(2024,12,24)),
                new Ingredient("Salt", 1000, "grams", 0.02, LocalDate.of(2024,12,24)),
                new Ingredient("Pepper", 50, "grams", 0.6, LocalDate.of(2024, 12, 24))
        );
    }

    public static List<Recipe> getPreDefineRecipes() {
        return List.of(
                new Recipe(
                        "Pannekake",
                        "Classic norwegian pancakes, thin and delicious",
                        "Mix all ingredients, pour batter in a pan and cook until golden",
                        List.of(
                                new Ingredient("Egg", 2, "pcs", 0.0, LocalDate.MAX),
                                new Ingredient("Milk", 0.25, "L", 0.0, LocalDate.MAX),
                                new Ingredient("Butter", 50, "grams", 0.0, LocalDate.MAX),
                                new Ingredient("Flour", 150, "grams", 0.0, LocalDate.MAX),
                                new Ingredient("Baking soda", 6, "grams", 0.0, LocalDate.MAX),
                                new Ingredient("Sugar", 9, "grams", 0.0, LocalDate.MAX)
                        ),
                        4
                ),
                new Recipe(
                        "Scrambled eggs",
                        "Classic scrambled eggs with milk, butter and chives",
                        "Melt butter in a pan, whisk egg and milk together, stir until just set",
                        List.of(
                                new Ingredient("Egg", 3, "pcs", 0.0, LocalDate.MAX),
                                new Ingredient("Milk", 0.15, "dL", 0.0, LocalDate.MAX),
                                new Ingredient("Butter", 30, "grams", 0.0, LocalDate.MAX),
                                new Ingredient("Salt", 0.4, "grams", 0.0, LocalDate.MAX),
                                new Ingredient("Pepper", 0.4,"grams", 0.0, LocalDate.MAX),
                                new Ingredient("Chives", 0.5, "pcs", 0.0, LocalDate.MAX)
                        ),
                        2
                ),
                new Recipe("Pasta with salmon",
                        "Creamy pasta with salmon and green peas",
                        "Cook pasta until al dente, fry salmon on both sides, heat up creme fraiche with peas and serve",
                        List.of(
                                new Ingredient("Pasta", 400, "grams", 0.0, LocalDate.MAX),
                                new Ingredient("Salmon", 400, "grams", 0.0, LocalDate.MAX),
                                new Ingredient("Peas", 200, "grams", 0.0, LocalDate.MAX),
                                new Ingredient("Creme fraiche", 3, "dL", 0.0, LocalDate.MAX),
                                new Ingredient("Butter", 30, "grams", 0.0, LocalDate.MAX),
                                new Ingredient("Salt", 0.4, "grams", 0.0, LocalDate.MAX),
                                new Ingredient("Pepper", 0.4, "grams", 0.0, LocalDate.MAX)
                        ),
                        2
                ),
                new Recipe("Apple crumble",
                "Simple and delicious dessert with apples",
                        "Mix sliced apples with sugar, top with a crumble of flour, butter, and sugar. Bake until golden",
                        List.of(
                                new Ingredient("Apple", 4, "pcs", 0.0, LocalDate.MAX),
                                new Ingredient("Sugar", 100, "grams", 0.0, LocalDate.MAX),
                                new Ingredient("Flour", 100, "grams", 0.0, LocalDate.MAX),
                                new Ingredient("Butter", 100, "grams", 0.0, LocalDate.MAX)
                        ),
                        4
                )
        );
    }
}