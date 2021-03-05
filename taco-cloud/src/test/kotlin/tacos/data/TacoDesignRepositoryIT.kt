package tacos.data

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.test.context.ContextConfiguration
import org.testcontainers.junit.jupiter.Testcontainers
import tacos.domain.TacoDesign
import tacos.domain.getIngredients

@Testcontainers
@SpringBootTest(properties = ["spring.main.allow-bean-definition-overriding=true"])
@ContextConfiguration(initializers = [PostgresContainerTestInitializer::class])
@EnableJpaAuditing
class TacoDesignRepositoryIT(@Autowired val repository: TacoDesignRepository) {

    private val designName = "my taco design"
    private val ingredients = getIngredients("COTO", "GRBF", "TMTO", "JACK", "SLSA")

    @Test
    fun canSaveNewTacoDesign() {
        val originalTacoDesign = TacoDesign(name=designName, ingredients=ingredients)
        val savedTacoDesign = repository.save(originalTacoDesign)

        fun assertExpectedSavedDesign(actualDesign: TacoDesign, description: String) {
            assertEquals(designName, actualDesign.name, "$description - Name not as expected")
            assertEquals(ingredients, actualDesign.ingredients, "$description - Ingredients not as expected")
            assertDateSetRecently(actualDesign.createdDate, description, "Created date")
            assertDateSetRecently(actualDesign.updatedDate, description, "Updated date")
        }

        assertExpectedSavedDesign(savedTacoDesign, "Saved design")
        assertExpectedSavedDesign(repository.findById(savedTacoDesign.id!!).get(), "Retrieved design")
    }

    @Test
    fun canUpdateExistingTacoDesign() {
        // Set up preexisting record
        val tacoDesign = TacoDesign(name=designName, ingredients=ingredients)
        val originalSavedDesign = repository.save(tacoDesign)
        val designId = originalSavedDesign.id?: fail("Test should get a returned ID")
        val originalCreatedDate = originalSavedDesign.createdDate
        val originalUpdatedDate = originalSavedDesign.updatedDate

        // Sleep for one second so can be sure that last modified date will change
        Thread.sleep(1000)

        // Run an update
        val newName = "New and improved!"
        val newIngredients = getIngredients("FLTO", "GRBF", "TMTO", "JACK", "SLSA")
        tacoDesign.name = newName
        tacoDesign.ingredients = newIngredients
        val updatedSavedTacoDesign = repository.save(tacoDesign)

        // Assert
        fun assertExpectedUpdatedDesign(actualDesign: TacoDesign, description: String) {
            assertEquals(newName, actualDesign.name, "$description - Name not as expected")
            assertEquals(newIngredients, actualDesign.ingredients, "$description - Ingredients not as expected")
            assertEquals(originalCreatedDate, actualDesign.createdDate, "$description - Created date not as expected")
            assertDateSetRecently(actualDesign.updatedDate, description, "Updated date")
            assertNotEquals(originalUpdatedDate, actualDesign.updatedDate, "Updated date not changed")
        }
        assertExpectedUpdatedDesign(updatedSavedTacoDesign, "Saved design")
        assertExpectedUpdatedDesign(repository.findById(designId).get(), "Retrieved design")
    }

}

