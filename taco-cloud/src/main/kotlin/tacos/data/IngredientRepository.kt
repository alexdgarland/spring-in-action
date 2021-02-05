package tacos.data

import tacos.domain.Ingredient

interface IngredientRepository {

    fun findAll(): Iterable<Ingredient>

    fun findOne(id: String): Ingredient

    fun save(ingredient: Ingredient): Ingredient

}
