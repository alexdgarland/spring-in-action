package tacos.data

import tacos.domain.Ingredient

interface IngredientRepository {

    fun findAll(): Iterable<Ingredient>

    fun findAllAsMap(): Map<String, Ingredient>

    fun findOne(ingredientId: String): Ingredient

    fun save(ingredient: Ingredient): Ingredient

}
