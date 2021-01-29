package tacos.domain

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

const val DESIGN_NAME = "Mi taco"
val ingredientList = listOf("COTO", "GRBF", "JACK", "TMTO", "LETC", "SLSA")

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TacoDesignViewModelTest {

    @Test
    fun `Conversion to domain object succeeds when all fields are non-null`() {
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

    fun incompleteViewModels(): Stream<Arguments> {
        return Stream.of(
            TacoDesignViewModel(null, ingredientList),
            TacoDesignViewModel(DESIGN_NAME, null)
        ).map { Arguments.of(it) }
    }

    @ParameterizedTest
    @MethodSource("incompleteViewModels")
    fun `Conversion to domain object throws ViewModelValidationException when any field is null`(
        viewModel: TacoDesignViewModel
    ) {
        assertThrows<ViewModelValidationException> { viewModel.toTacoDesign() }
    }

    @Test
    fun `Conversion to domain object throws ViewModelValidationException when any ingredient not available`() {
        val viewModel = TacoDesignViewModel(
            DESIGN_NAME,
            listOf("Oh no, an ingredient that doesn't exist due to a race condition...")
        )
        assertThrows<ViewModelValidationException> { viewModel.toTacoDesign() }
    }

    @Test
    fun `Conversion to domain object throws ViewModelValidationException when ingredient list is empty`() {
        val viewModel = TacoDesignViewModel(DESIGN_NAME, emptyList())
        assertThrows<ViewModelValidationException> { viewModel.toTacoDesign() }
    }

}
