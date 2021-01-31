package tacos.domain

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import javax.validation.Validation

const val DESIGN_NAME = "Mi taco"
val ingredientList = listOf("COTO", "GRBF", "JACK", "TMTO", "LETC", "SLSA")

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TacoDesignViewModelTest {

    private val validator = run { Validation.buildDefaultValidatorFactory().validator }

    fun incompleteViewModels(): Stream<Arguments> {
        return Stream.of(
            Arguments.of(TacoDesignViewModel(ingredients = ingredientList), "Name must be at least 5 characters long"),
            Arguments.of(TacoDesignViewModel(name = DESIGN_NAME), "You must choose at least one ingredient"),
        )
    }

    @ParameterizedTest
    @MethodSource("incompleteViewModels")
    fun `Validation enforces that all fields are set`(
        viewModel: TacoDesignViewModel,
        expectedErrorMessage: String
    ) {
        val violations = validator.validate(viewModel)
        assertEquals(expectedErrorMessage, violations.single().message)
    }

    @Test
    fun `Conversion to domain object succeeds when all fields are valid`() {
        val viewModel = TacoDesignViewModel(DESIGN_NAME, ingredientList)

        val domainObject = viewModel.toTacoDesign()

        val expectedIngredients = listOf(
            Ingredient("COTO", "Corn Tortilla", IngredientType.WRAP),
            Ingredient("GRBF", "Ground Beef", IngredientType.PROTEIN),
            Ingredient("JACK", "Monterrey Jack", IngredientType.CHEESE),
            Ingredient("TMTO", "Diced Tomatoes", IngredientType.VEGGIES),
            Ingredient("LETC", "Lettuce", IngredientType.VEGGIES),
            Ingredient("SLSA", "Salsa", IngredientType.SAUCE)
        )
        assertEquals(DESIGN_NAME, domainObject.name)
        assertEquals(expectedIngredients, domainObject.ingredients)
    }

    @Test
    fun `Conversion to domain object throws ViewModelConversionException when any ingredient not available`() {
        val viewModel = TacoDesignViewModel(
            DESIGN_NAME,
            listOf("Oh no, an ingredient that doesn't exist due to a race condition...")
        )
        assertThrows<ViewModelConversionException> { viewModel.toTacoDesign() }
    }


}
