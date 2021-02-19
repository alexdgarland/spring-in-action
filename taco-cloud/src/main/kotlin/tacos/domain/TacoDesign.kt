package tacos.domain

import java.util.*
import javax.validation.constraints.Size

data class TacoDesign(
    var id: Long? = null,
    @get:Size(min=5, message = "Name must be at least 5 characters long")
    var name: String = "",
    @get:Size(min=1, message = "You must choose at least one ingredient")
    var ingredients: List<String> = emptyList(),
    val createdDate: Date? = null,
    val updatedDate: Date? = null
) {

    fun getIngredientUiMap(
        availableIngredients: Iterable<Ingredient>
    ): Map<String, List<IngredientCheckBoxViewModel>> {
        return availableIngredients
            .groupBy { ingredient -> ingredient.type.toString().toLowerCase() }
            .mapValues { entry ->
                entry.value.map { ingredient ->
                    IngredientCheckBoxViewModel(
                        id = ingredient.id,
                        name = ingredient.name,
                        checked = ingredients.contains(ingredient.id))
                }
            }
    }

    fun getWithIngredientDescriptions(ingredientMap: Map<String, Ingredient>): TacoDesignWithIngredientDescriptions {
        val fullIngredients = ingredients.map {
            ingredientMap.get(it)?.description?: throw ViewModelConversionException("Cannot get ingredient for ID $it")
        }
        val notNullId = id?: throw ViewModelConversionException("ID is not set for taco design with name $name")
        return TacoDesignWithIngredientDescriptions(notNullId, name, fullIngredients)
    }

}

data class TacoDesignWithIngredientDescriptions(
    var id: Long,
    var name: String,
    var ingredientDescriptions: List<String>
)
