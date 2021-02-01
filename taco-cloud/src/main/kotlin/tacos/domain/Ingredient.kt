package tacos.domain

enum class IngredientType { WRAP, PROTEIN, VEGGIES, CHEESE, SAUCE }

data class Ingredient(val id: String, val name: String, val type: IngredientType)

data class IngredientCheckBoxViewModel(val id: String, val name: String, val checked: Boolean)
