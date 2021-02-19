package tacos.data.impl.postgresql

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.testcontainers.junit.jupiter.Testcontainers
import tacos.domain.Ingredient
import tacos.domain.IngredientType

@Testcontainers
class PostgresqlIngredientRepositoryIT: PostgresqlIntegrationTestWithContainer() {

    private val repository
    get() = PostgresqlIngredientRepository(template)

    @Test
    fun canSaveAndRetrieveIngredient() {
        // Use an ingredient not in the data setup script
        val originalIngredient = Ingredient("BLTO", "Blue Corn Tortilla", IngredientType.WRAP)

        val savedIngredient = repository.save(originalIngredient)
        assertEquals(originalIngredient, savedIngredient)

        val retrievedIngredient = repository.findOne("BLTO")
        assertEquals(originalIngredient, retrievedIngredient)
    }

    @Test
    fun canUpdateIngredient() {
        val ingredientId = "SZCH"

        fun saveThenCheckName(ingredientName: String) {
            val ingredientToSave = Ingredient(ingredientId, ingredientName, IngredientType.SAUCE)
            repository.save(ingredientToSave)
            val retrievedIngredient = repository.findOne(ingredientId)
            assertEquals(ingredientName, retrievedIngredient.name)
        }

        saveThenCheckName("Szechuan Sauce")
        saveThenCheckName("Unusual sauce for a taco")
    }

    @Test
    fun canRetrieveAllIngredients() {
        val expectedIngredients = listOf(
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

        assertEquals(expectedIngredients, repository.findAll())
    }

    @Test
    fun canRetrieveAllIngredientsAsMap() {
        val expectedMap = mapOf(
            "FLTO" to Ingredient("FLTO", "Flour Tortilla", IngredientType.WRAP),
            "COTO" to Ingredient("COTO", "Corn Tortilla", IngredientType.WRAP),
            "GRBF" to Ingredient("GRBF", "Ground Beef", IngredientType.PROTEIN),
            "CARN" to Ingredient("CARN", "Carnitas", IngredientType.PROTEIN),
            "TMTO" to Ingredient("TMTO", "Diced Tomatoes", IngredientType.VEGGIES),
            "LETC" to Ingredient("LETC", "Lettuce", IngredientType.VEGGIES),
            "CHED" to Ingredient("CHED", "Cheddar", IngredientType.CHEESE),
            "JACK" to Ingredient("JACK", "Monterrey Jack", IngredientType.CHEESE),
            "SLSA" to Ingredient("SLSA", "Salsa", IngredientType.SAUCE),
            "SRCR" to Ingredient("SRCR", "Sour Cream", IngredientType.SAUCE)
        )

        assertEquals(expectedMap, repository.findAllAsMap())
    }

}
