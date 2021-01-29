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

    fun toTacoDesign(): TacoDesign {
        return TacoDesign(
            name!!,
            ingredients!!.map { orderedIngredient ->
                availableIngredients.filter {
                    it.id == orderedIngredient
                }.first()
            }
            //  This would also succeed (and mightly be marginally more efficient) but does not preserve ordering:
            //      availableIngredients.filter { ingredients!!.contains(it.id) }
        )
    }

}
