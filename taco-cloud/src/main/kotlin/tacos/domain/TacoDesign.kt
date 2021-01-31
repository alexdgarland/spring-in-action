package tacos.domain

import tacos.web.availableIngredients
import javax.validation.constraints.Size

data class TacoDesign(
    val name: String,
    val ingredients: List<Ingredient>
)

data class TacoDesignViewModel(
    @get:Size(min=5, message = "Name must be at least 5 characters long")
    var name: String = "",
    @get:Size(min=1, message = "You must choose at least one ingredient")
    var ingredients: List<String> = emptyList()
) {

    private fun convertIngredientsFromStrings(ingredients: List<String>): List<Ingredient> {
        return ingredients.map { orderedIngredient ->
            try {
                availableIngredients.filter { it.id == orderedIngredient }.first()
            }
            catch (nsee: NoSuchElementException) {
                throw ViewModelConversionException("Ingredient \"$orderedIngredient\" is not available", nsee)
            }
        }
        //  The following  would also work (and mightly be marginally more efficient):
        //      availableIngredients.filter { ingredients.contains(it.id) }
        //  ...but does not preserve ordering and would require a different approach to throw an exception
        //  where ingredients are not available
    }

    fun toTacoDesign(): TacoDesign {
        return TacoDesign(
            name,
            convertIngredientsFromStrings(ingredients)
        )
    }

}
