package tacos.web

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import tacos.domain.IngredientCheckBoxViewModel
import tacos.domain.IngredientType
import tacos.domain.TacoDesignViewModel


class DesignTacoControllerTest {

    @Test
    fun `getCheckBoxes function should return expected map (all unchecked) for empty design model`() {
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

        val actualMap = getCheckBoxes(availableIngredients, TacoDesignViewModel())

        assertEquals(expectedMap, actualMap)
    }

    @Test
    fun `getCheckBoxes function should return expected map (some checked) for populated design model`() {
        val designViewModel = TacoDesignViewModel("My taco design", listOf("COTO", "GRBF", "TMTO", "JACK", "SLSA"))
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

        val actualMap = getCheckBoxes(availableIngredients, designViewModel)

        assertEquals(expectedMap, actualMap)
    }

}