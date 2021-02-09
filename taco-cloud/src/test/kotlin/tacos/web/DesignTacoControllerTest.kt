package tacos.web

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import tacos.domain.Ingredient
import tacos.domain.IngredientCheckBoxViewModel
import tacos.domain.IngredientType
import tacos.domain.TacoDesign

val availableIngredients = listOf(
    Ingredient("FLTO", "Flour Tortilla", IngredientType.WRAP),
    Ingredient("COTO", "Corn Tortilla", IngredientType.WRAP),
    Ingredient("GRBF", "Ground Beef", IngredientType.PROTEIN),
    Ingredient("CARN", "Carnitas", IngredientType.PROTEIN),
    Ingredient("TMTO", "Diced Tomatoes", IngredientType.VEGGIES),
    Ingredient("LETC", "Lettuce", IngredientType.VEGGIES),
    Ingredient("CHED", "Cheddar", IngredientType.CHEESE),
    Ingredient("JACK", "Monterrey Jack", IngredientType.CHEESE),
    Ingredient("SLSA", "Salsa", IngredientType.SAUCE),
    Ingredient("SRCR", "Sour Cream", IngredientType.SAUCE)
)

class DesignTacoControllerTest {

    @Test
    fun `getIngredientUiMap function should return expected map (all unchecked) for empty design model`() {
        val expectedMap = mapOf(
            "wrap" to listOf(
                IngredientCheckBoxViewModel("FLTO", "Flour Tortilla", false),
                IngredientCheckBoxViewModel("COTO", "Corn Tortilla", false)
            ),
            "protein" to listOf(
                IngredientCheckBoxViewModel("GRBF", "Ground Beef", false),
                IngredientCheckBoxViewModel("CARN","Carnitas", false)
            ),
            "veggies" to listOf(
                IngredientCheckBoxViewModel("TMTO", "Diced Tomatoes", false),
                IngredientCheckBoxViewModel("LETC","Lettuce", false)
            ),
            "cheese" to listOf(
                IngredientCheckBoxViewModel("CHED", "Cheddar", false),
                IngredientCheckBoxViewModel("JACK","Monterrey Jack", false)
            ),
            "sauce" to listOf(
                IngredientCheckBoxViewModel("SLSA", "Salsa", false),
                IngredientCheckBoxViewModel("SRCR","Sour Cream", false)
            )
        )

        val actualMap = getIngredientUiMap(availableIngredients, TacoDesign())

        assertEquals(expectedMap, actualMap)
    }

    @Test
    fun `getIngredientUiMap function should return expected map (some checked) for populated design model`() {
        val designViewModel = TacoDesign(100, "My taco design", listOf("COTO", "GRBF", "TMTO", "JACK", "SLSA"))
        val expectedMap = mapOf(
            "wrap" to listOf(
                IngredientCheckBoxViewModel("FLTO", "Flour Tortilla", false),
                IngredientCheckBoxViewModel("COTO", "Corn Tortilla", true)
            ),
            "protein" to listOf(
                IngredientCheckBoxViewModel("GRBF", "Ground Beef", true),
                IngredientCheckBoxViewModel("CARN","Carnitas", false)
            ),
            "veggies" to listOf(
                IngredientCheckBoxViewModel("TMTO", "Diced Tomatoes", true),
                IngredientCheckBoxViewModel("LETC","Lettuce", false)
            ),
            "cheese" to listOf(
                IngredientCheckBoxViewModel("CHED", "Cheddar", false),
                IngredientCheckBoxViewModel("JACK","Monterrey Jack", true)
            ),
            "sauce" to listOf(
                IngredientCheckBoxViewModel("SLSA", "Salsa", true),
                IngredientCheckBoxViewModel("SRCR","Sour Cream", false)
            )
        )

        val actualMap = getIngredientUiMap(availableIngredients, designViewModel)

        assertEquals(expectedMap, actualMap)
    }

}
