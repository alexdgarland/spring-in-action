package tacos.domain

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import javax.validation.Validation

const val DESIGN_NAME = "Mi taco"

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TacoDesignTest {

    private val validator = run { Validation.buildDefaultValidatorFactory().validator }

    fun incompleteDesigns(): Stream<Arguments> {
        return Stream.of(
            Arguments.of(TacoDesign(ingredients = availableIngredients), "Name must be at least 5 characters long"),
            Arguments.of(TacoDesign(name = DESIGN_NAME), "You must choose at least one ingredient"),
        )
    }

    @ParameterizedTest
    @MethodSource("incompleteDesigns")
    fun `Validation enforces that all fields are set`(design: TacoDesign, expectedErrorMessage: String) {
        val violations = validator.validate(design)
        assertEquals(expectedErrorMessage, violations.single().message)
    }

    @Test
    fun `getIngredientUiMap should return expected map (all unchecked) for empty design model`() {
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

        val actualMap = TacoDesign().getIngredientUiMap(availableIngredients)

        assertEquals(expectedMap, actualMap)
    }

    @Test
    fun `getIngredientUiMap should return expected map (some checked) for populated design model`() {
        val design = TacoDesign(100, "My taco design",
            getIngredients("COTO", "GRBF", "TMTO", "JACK", "SLSA"))
        val expectedMap = mapOf(
            "wrap" to listOf(
                IngredientCheckBoxViewModel("FLTO", "Flour Tortilla", false),
                IngredientCheckBoxViewModel("COTO", "Corn Tortilla", true)
            ),
            "protein" to listOf(
                IngredientCheckBoxViewModel("GRBF", "Ground Beef", true),
                IngredientCheckBoxViewModel("CARN", "Carnitas", false)
            ),
            "veggies" to listOf(
                IngredientCheckBoxViewModel("TMTO", "Diced Tomatoes", true),
                IngredientCheckBoxViewModel("LETC", "Lettuce", false)
            ),
            "cheese" to listOf(
                IngredientCheckBoxViewModel("CHED", "Cheddar", false),
                IngredientCheckBoxViewModel("JACK", "Monterrey Jack", true)
            ),
            "sauce" to listOf(
                IngredientCheckBoxViewModel("SLSA", "Salsa", true),
                IngredientCheckBoxViewModel("SRCR", "Sour Cream", false)
            )
        )

        val actualMap = design.getIngredientUiMap(availableIngredients)

        assertEquals(expectedMap, actualMap)
    }

}
