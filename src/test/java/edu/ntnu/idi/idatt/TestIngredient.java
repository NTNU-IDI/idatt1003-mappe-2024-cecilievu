package edu.ntnu.idi.idatt;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test-klasse som skal sjekke om Ingredient-klassen funker
 *
 * <p>Disse metodende testes:</p>
 *
 * <b>Positive tester:</b>
 *
 * <ul>
 *     <li>Oppretter en vare med gyldig variabler (name, quantity, unit, pricePerUnit og bestBefore)</li>
 *     <li>Accessor-method skal returnere riktig navn</li>
 *     <li>Accessor-method skal returnere riktig quantity</li>
 *     <li>Accessor-method skal returnere riktig unit</li>
 * </ul>
 */

public class TestIngredient {
    @Test
    public void testCreateIngredientWithValidParameters() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate bestBeforeDate = LocalDate.parse("01-11-2024", formatter);
        Ingredient ingredient = new Ingredient("Egg", 12, "pieces", 2.0, bestBeforeDate);

        assertEquals("Egg", ingredient.getNameItem());
        assertEquals(12, ingredient.getQuantityItem());
        assertEquals("pieces", ingredient.getUnitItem());
        assertEquals(2.0, ingredient.getPricePerUnit());
        assertEquals(bestBeforeDate, ingredient.getBestBefore());
    }

    @Test public void testSetQuantityItemValidValue() {
        Ingredient ingredient = new Ingredient("Milk", 1.5, "L", 13.3, LocalDate.now().plusDays(10));
        ingredient.setQuantityItem(10);
        assertEquals(10, ingredient.getQuantityItem());
    }

    //negativ test
    @Test public void testSetQuantityItemInvalidValue() {
        Ingredient ingredient = new Ingredient("Milk", 1.5, "L", 13.3, LocalDate.now());
        assertThrows(IllegalArgumentException.class, () -> ingredient.setQuantityItem(-5));
    }

}