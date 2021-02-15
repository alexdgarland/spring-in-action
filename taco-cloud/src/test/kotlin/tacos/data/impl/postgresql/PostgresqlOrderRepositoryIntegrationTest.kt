package tacos.data.impl.postgresql

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.fail
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

    private val designRepository
    get() = PostgresqlTacoDesignRepository(template, dateProvider)

    private val design1 by lazy {
        designRepository.save(TacoDesign(name = "my first taco", ingredients = listOf("COTO", "CARN")))
    }

    private val design2 by lazy {
        designRepository.save(TacoDesign(name = "my second taco", ingredients = listOf("FLTO", "GRBF")))
    }

    @Test
    fun canSaveNewOrder() {
        val tacoDesigns = mutableListOf(design1, design2)

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
        assertEquals(expectedSavedOrder, repository.findOne(expectedOrderId))
    }

    @Test
    fun canUpdateExistingOrder() {
        // Set up preexisting record
        val originalDate = SimpleDateFormat("yyyy-MM-dd").parse("2021-01-23")
        val originalOrder = Order(name = orderName, street = orderStreet, city = orderCity, state = orderState,
            zip = orderZip, ccNumber = orderCcNumber, ccExpiration = orderCcExpiration, ccCvv = orderCcv,
            tacoDesigns = mutableListOf(design1, design2), placedDate=originalDate, updatedDate=originalDate
        )
        val orderId = repository.save(originalOrder).id?: fail("Test should get a returned ID")

        // Run an update
        val newName = "New and improved!"
        val newDesigns = mutableListOf(
            design1,
            designRepository.save(TacoDesign(name = "my third taco", ingredients = listOf("COTO", "GRBF", "SLSA")))
        )
        val updatedOrder = Order(id = orderId, name = newName, street = orderStreet, city = orderCity, state = orderState,
            zip = orderZip, ccNumber = orderCcNumber, ccExpiration = orderCcExpiration, ccCvv = orderCcv,
            tacoDesigns = newDesigns, placedDate=originalDate, updatedDate=originalDate
        )
        val savedOrder = repository.save(updatedOrder)

        // Assert
        val expectedSavedOrder = Order(id = orderId, name = newName, street = orderStreet, city = orderCity, state = orderState,
            zip = orderZip, ccNumber = orderCcNumber, ccExpiration = orderCcExpiration, ccCvv = orderCcv,
            tacoDesigns = newDesigns, placedDate=originalDate, updatedDate=testDate
        )
        assertEquals(expectedSavedOrder, savedOrder)
        assertEquals(expectedSavedOrder, repository.findOne(orderId))
    }

}
