package tacos.data.impl.postgresql

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.fail
import org.junit.jupiter.api.Test
import org.testcontainers.junit.jupiter.Testcontainers
import tacos.domain.TacoDesign
import java.text.SimpleDateFormat

@Testcontainers
class PostgresqlTacoDesignRepositoryIT: PostgresqlIntegrationTestWithContainer() {

    private val designName = "my taco design"
    private val ingredients = listOf("COTO", "GRBF", "TMTO", "JACK", "SLSA")
    private val testDate = SimpleDateFormat("yyyy-MM-dd").parse("2021-01-24")
    private val dateProvider = FixedTestDateProvider(testDate)

    private val repository
    get() = PostgresqlTacoDesignRepository(template, dateProvider)

    @Test
    fun canSaveNewTacoDesign() {
        val originalTacoDesign = TacoDesign(name=designName, ingredients=ingredients)

        val savedTacoDesign = repository.save(originalTacoDesign)

        val expectedId: Long = 1
        val expectedSavedTacoDesign = TacoDesign(
            id=expectedId, name=designName, ingredients=ingredients, createdDate=testDate, updatedDate=testDate
        )
        assertEquals(expectedSavedTacoDesign, savedTacoDesign)
        assertEquals(expectedSavedTacoDesign, repository.findOne(expectedId))
    }

    @Test
    fun canUpdateExistingTacoDesign() {
        // Set up preexisting record
        val originalDate = SimpleDateFormat("yyyy-MM-dd").parse("2021-01-23")
        val originalTacoDesign = TacoDesign(
            name=designName, ingredients=ingredients, createdDate=originalDate, updatedDate=originalDate
        )
        val designId = repository.save(originalTacoDesign).id?: fail("Test should get a returned ID")

        // Run an update
        val newName = "New and improved!"
        val newIngredients = listOf("FLTO", "GRBF", "TMTO", "JACK", "SLSA")
        val updatedTacoDesign = TacoDesign(
            id=designId, name=newName, ingredients=newIngredients, createdDate=originalDate, updatedDate=originalDate
        )
        val savedTacoDesign = repository.save(updatedTacoDesign)

        // Assert
        val expectedSavedTacoDesign = TacoDesign(
            id=designId, name=newName, ingredients=newIngredients,createdDate=originalDate, updatedDate=testDate
        )
        assertEquals(expectedSavedTacoDesign, savedTacoDesign)
        assertEquals(expectedSavedTacoDesign, repository.findOne(designId))
    }

}
