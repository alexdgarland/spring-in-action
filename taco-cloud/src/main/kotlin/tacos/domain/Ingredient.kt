package tacos.domain

enum class IngredientType { WRAP, PROTEIN, VEGGIES, CHEESE, SAUCE }

data class Ingredient(var id: String, var name: String, var type: IngredientType) {
    val description get() = "$id - $name ($type)"
}

data class IngredientCheckBoxViewModel(val id: String, val name: String, val checked: Boolean)
