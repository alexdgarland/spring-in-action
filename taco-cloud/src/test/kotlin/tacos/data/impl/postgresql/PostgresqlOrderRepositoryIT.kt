package tacos.data.impl.postgresql

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.fail
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.ContextConfiguration
import org.testcontainers.junit.jupiter.Testcontainers
import tacos.data.PostgresContainerTestInitializer
import tacos.data.TacoDesignRepository
import tacos.domain.Order
import tacos.domain.TacoDesign
import tacos.domain.getIngredients
import java.text.SimpleDateFormat
import javax.transaction.Transactional


@Testcontainers
@SpringBootTest
@ContextConfiguration(initializers = [PostgresContainerTestInitializer::class])
@Transactional
class PostgresqlOrderRepositoryIT {

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

    @Autowired
    private lateinit var designRepository: TacoDesignRepository

    @Autowired
    private lateinit var template: JdbcTemplate

    private val repository
    get() = PostgresqlOrderRepository(template, dateProvider, designRepository)

    private val design1 by lazy {
        designRepository.save(TacoDesign(name = "my first taco", ingredients = getIngredients("COTO", "CARN")))
    }

    private val design2 by lazy {
        designRepository.save(TacoDesign(name = "my second taco", ingredients = getIngredients("FLTO", "GRBF")))
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
        val retrievedOrder = repository.findOne(expectedOrderId)
        println(retrievedOrder)
        assertEquals(expectedSavedOrder, retrievedOrder)
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
        val design3 = designRepository.save(
            TacoDesign(
                name = "my third taco",
                ingredients = getIngredients("COTO", "GRBF", "SLSA")
            )
        )
        val newDesigns = mutableListOf(design1, design3)
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
