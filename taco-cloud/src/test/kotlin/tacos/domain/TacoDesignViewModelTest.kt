package tacos.domain

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

const val DESIGN_NAME = "Mi taco"

class TacoDesignViewModelTest {

    @Test
    fun conversionToDomainObjectSucceedsWhenAllFieldsNonNull() {
        val viewModel = TacoDesignViewModel(DESIGN_NAME, listOf("COTO", "GRBF", "JACK", "TMTO", "LETC", "SLSA"))

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

    // TODO
    //  - test error where nulls
    //  - test error where ingredient not in available list (e.g. due to weird race condition)
    //  - test error where ingredient list empty?

}
