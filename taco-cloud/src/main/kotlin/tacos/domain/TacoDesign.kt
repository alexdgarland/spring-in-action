package tacos.domain

import tacos.web.availableIngredients

data class TacoDesign(
    var name: String,
    var ingredients: List<Ingredient>
)

data class TacoDesignViewModel(
    var name: String? = null,
    var ingredients: List<String>? = null
) {

    private fun convertIngredientsFromStrings(ingredients: List<String>): List<Ingredient> {
        if (ingredients.isEmpty()) {
            throw ViewModelValidationException("No ingredients were selected")
        }
        return ingredients.map { orderedIngredient ->
            try {
                availableIngredients.filter { it.id == orderedIngredient }.first()
            }
            catch (nsee: NoSuchElementException) {
                throw ViewModelValidationException("Ingredient \"$orderedIngredient\" is not available", nsee)
            }
        }
        //  The following  would also work (and mightly be marginally more efficient):
        //
        //      availableIngredients.filter { ingredients.contains(it.id) }
        //
        //  ...but does not preserve ordering and would require a different approach to throw an exception
        //  where ingredients are not available
    }

    fun toTacoDesign(): TacoDesign {
        return TacoDesign(
            validateNonNull(name, "name"),
            convertIngredientsFromStrings(validateNonNull(ingredients, "ingredients"))
        )
    }

}
