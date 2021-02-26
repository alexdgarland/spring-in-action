package tacos.domain

val availableIngredients = listOf(
    Ingredient("FLTO", "Flour Tortilla", IngredientType.WRAP),
    Ingredient("COTO", "Corn Tortilla", IngredientType.WRAP),
    Ingredient("GRBF", "Ground Beef", IngredientType.PROTEIN),
    Ingredient("CARN", "Carnitas", IngredientType.PROTEIN),
    Ingredient("TMTO", "Diced Tomatoes", IngredientType.VEGGIES),
    Ingredient("LETC", "Lettuce", IngredientType.VEGGIES),
    Ingredient("CHED", "Cheddar", IngredientType.CHEESE),
    Ingredient("JACK", "Monterrey Jack", IngredientType.CHEESE),
    Ingredient("SLSA", "Salsa", IngredientType.SAUCE),
    Ingredient("SRCR", "Sour Cream", IngredientType.SAUCE)
)

fun getIngredients(vararg ids: String): List<Ingredient> {
    return availableIngredients.filter { ids.contains(it.id) }
}
