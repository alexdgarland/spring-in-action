package tacos.data.impl.postgresql

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.testcontainers.junit.jupiter.Testcontainers
import tacos.domain.TacoDesign
import java.text.SimpleDateFormat
import java.util.*

class FixedTestDateProvider(val date: Date): DateProvider {

    override fun getCurrentDate(): Date {
        return date
    }

}

@Testcontainers
class PostgresqlTacoDesignRepositoryIntegrationTest: AbstractPostgresqlRepositoryTest() {

    private val designName = "my taco design"
    private val ingredients = listOf("COTO", "GRBF", "TMTO", "JACK", "SLSA")
    private val testDate = SimpleDateFormat("yyyy-MM-dd").parse("2021-01-24")
    private val dateProvider = FixedTestDateProvider(testDate)

    @Test
    fun canSaveTacoDesignWithNoExistingId() {
        val repository = PostgresqlTacoDesignRepository(template, dateProvider)
        val originalTacoDesign = TacoDesign(name=designName, ingredients=ingredients)

        val savedTacoDesign = repository.save(originalTacoDesign)
        val expectedSavedTacoDesign = TacoDesign(id=1, name=designName, ingredients=ingredients, createdDate = testDate)
        assertEquals(expectedSavedTacoDesign, savedTacoDesign)

        // TODO - retrieve using repository.findOne(savedTacoDesign.id) and make sure it comes back as expected
    }


// TODO - add a test where ID is already populated (update existing record)

}
