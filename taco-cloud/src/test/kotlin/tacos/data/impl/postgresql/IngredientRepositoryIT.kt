package tacos.data.impl.postgresql

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.test.context.ContextConfiguration
import org.testcontainers.junit.jupiter.Testcontainers
import tacos.data.IngredientRepository
import tacos.data.KPostgreSQLTestContainer
import tacos.domain.Ingredient
import tacos.domain.IngredientType

class PostgresContainerTestInitializer: ApplicationContextInitializer<ConfigurableApplicationContext> {

    override fun initialize(appContext: ConfigurableApplicationContext) {
        val postgresContainer: KPostgreSQLTestContainer = KPostgreSQLTestContainer.create()
            .withInitScript("schema.sql", "01-schema.sql")
            .withInitScript("data.sql", "02-data.sql")
        postgresContainer.start()

        TestPropertyValues.of(
            "spring.datasource.url=${postgresContainer.jdbcUrl}",
            "spring.datasource.username=${postgresContainer.username}",
            "spring.datasource.password=${postgresContainer.password}"
        ).applyTo(appContext)
    }

}

@Testcontainers
@SpringBootTest
@ContextConfiguration(initializers = [PostgresContainerTestInitializer::class])
class IngredientRepositoryIT {

    @Autowired
    private lateinit var repository: IngredientRepository

    @Test
    fun canSaveAndRetrieveIngredient() {
        // Use an ingredient not in the data setup script
        val originalIngredient = Ingredient("BLTO", "Blue Corn Tortilla", IngredientType.WRAP)

        val savedIngredient = repository.save(originalIngredient)
        assertEquals(originalIngredient, savedIngredient)

        val retrievedIngredient = repository.findById("BLTO").get()
        assertEquals(originalIngredient, retrievedIngredient)

        // Clean-up the additional ingredient to leave DB ready for other tests, and assert that method works as well
        repository.deleteById("BLTO")
        assertTrue(repository.findById("BLTO").isEmpty)
    }

    @Test
    fun canUpdateIngredient() {
        val ingredientId = "SZCH"

        fun saveThenCheckName(ingredientName: String) {
            val ingredientToSave = Ingredient(ingredientId, ingredientName, IngredientType.SAUCE)
            repository.save(ingredientToSave)
            val retrievedIngredient = repository.findById(ingredientId).get()
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

}
