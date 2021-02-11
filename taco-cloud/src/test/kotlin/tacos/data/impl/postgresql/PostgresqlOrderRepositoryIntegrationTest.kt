package tacos.data.impl.postgresql

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.testcontainers.junit.jupiter.Testcontainers
import tacos.domain.Order
import tacos.domain.TacoDesign
import java.text.SimpleDateFormat


@Testcontainers
class PostgresqlOrderRepositoryIntegrationTest: AbstractPostgresqlRepositoryTest() {

    private val orderName = "my order"
    private val orderStreet = "25 Some Street"
    private val orderCity = "Cityton"
    private val orderState = "NY"
    private val orderZip = "ZIP001"
    private val orderCcNumber = "345678920"
    private val orderCcExpiration = "05/25"
    private val orderCcv = "123"

    private val testDate = SimpleDateFormat("yyyy-MM-dd").parse("2021-01-24")
    private val dateProvider = FixedTestDateProvider(testDate)

    private val repository
    get() = PostgresqlOrderRepository(template, dateProvider)

    @Test
    fun canSaveNewOrder() {
        // Setup - pre-save some taco designs
        val designRepository = PostgresqlTacoDesignRepository(template, dateProvider)
        val tacoDesigns = mutableListOf(
            designRepository.save(TacoDesign(name = "my first taco", ingredients = listOf("COTO", "CARN"))),
            designRepository.save(TacoDesign(name = "my second taco", ingredients = listOf("FLTO", "GRBF")))
        )

        val originalOrder = Order(name = orderName, street = orderStreet, city = orderCity, state = orderState,
            zip = orderZip, ccNumber = orderCcNumber, ccExpiration = orderCcExpiration, ccCvv = orderCcv,
            tacoDesigns = tacoDesigns
        )
        val savedOrder = repository.save(originalOrder)

        val expectedOrderId: Long = 1
        val expectedSavedOrder = Order(
            id=expectedOrderId, name = orderName, street = orderStreet, city = orderCity, state = orderState,
            zip = orderZip, ccNumber = orderCcNumber, ccExpiration = orderCcExpiration, ccCvv = "123",
            tacoDesigns = tacoDesigns, placedDate = testDate, updatedDate = testDate
        )
        assertEquals(expectedSavedOrder, savedOrder)
//        assertEquals(expectedSavedTacoDesign, repository.findOne(expectedId))
    }

    @Test
    fun canUpdateExistingOrder() {
//        // Set up preexisting record
//        val originalDate = SimpleDateFormat("yyyy-MM-dd").parse("2021-01-23")
//        val originalTacoDesign = TacoDesign(
//            name=designName, ingredients=ingredients, createdDate=originalDate, updatedDate=originalDate
//        )
//        val designId = repository.save(originalTacoDesign).id?: fail("Test should get a returned ID")
//
//        // Run an update
//        val newName = "New and improved!"
//        val newIngredients = listOf("FLTO", "GRBF", "TMTO", "JACK", "SLSA")
//        val updatedTacoDesign = TacoDesign(
//            id=designId, name=newName, ingredients=newIngredients, createdDate=originalDate, updatedDate=originalDate
//        )
//        val savedTacoDesign = repository.save(updatedTacoDesign)
//
//        // Assert
//        val expectedSavedTacoDesign = TacoDesign(
//            id=designId, name=newName, ingredients=newIngredients,createdDate=originalDate, updatedDate=testDate
//        )
//        assertEquals(expectedSavedTacoDesign, savedTacoDesign)
//        assertEquals(expectedSavedTacoDesign, repository.findOne(designId))
    }

}
