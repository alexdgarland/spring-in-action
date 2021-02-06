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
        val originalIngredient = Ingredient("COTO", "Corn Tortilla", IngredientType.WRAP)

        val savedIngredient = repository.save(originalIngredient)
        assertEquals(originalIngredient, savedIngredient)

        val retrievedIngredient = repository.findOne("COTO")
        assertEquals(originalIngredient, retrievedIngredient)
    }

    // TODO - for this table we always set the ID ourselves,
    //      but for other tables we would want to add a test where ID is initially missing in the in-memory model
    //      and is set on upsert to database - like https://www.postgresql.org/docs/9.3/dml-returning.html

}
