package tacos.data.impl.postgresql

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.testcontainers.junit.jupiter.Testcontainers
import tacos.domain.Ingredient
import tacos.domain.IngredientType

@Testcontainers
class PostgresqlIngredientRepositoryIntegrationTest(): AbstractPostgresqlRepositoryTest() {

    @Test
    fun canSaveAndRetrieveIngredient() {
        val repository = PostgresqlIngredientRepository(template)
        // Use an ingredient not in the data setup script
        val originalIngredient = Ingredient("BLTO", "Blue Corn Tortilla", IngredientType.WRAP)

        val savedIngredient = repository.save(originalIngredient)
        assertEquals(originalIngredient, savedIngredient)

        val retrievedIngredient = repository.findOne("BLTO")
        assertEquals(originalIngredient, retrievedIngredient)
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

        val repository = PostgresqlIngredientRepository(template)
        val retrievedIngredients = repository.findAll()

        assertEquals(expectedIngredients, retrievedIngredients)
    }

    // TODO - for this table we always set the ID ourselves,
    //      but for other tables we would want to add a test where ID is initially missing in the in-memory model
    //      and is set on upsert to database - like https://www.postgresql.org/docs/9.3/dml-returning.html

}
