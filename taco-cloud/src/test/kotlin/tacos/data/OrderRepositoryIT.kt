package tacos.data

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.test.context.ContextConfiguration
import org.testcontainers.junit.jupiter.Testcontainers
import tacos.domain.Order
import tacos.domain.TacoDesign
import tacos.domain.getIngredients
import java.util.*

@Testcontainers
@SpringBootTest(properties = ["spring.main.allow-bean-definition-overriding=true"])
@ContextConfiguration(initializers = [PostgresContainerTestInitializer::class])
@EnableJpaAuditing
class OrderRepositoryIT(@Autowired val repository: OrderRepository) {

    private val orderName = "my order"
    private val orderStreet = "25 Some Street"
    private val orderCity = "Cityton"
    private val orderState = "NY"
    private val orderZip = "ZIP001"
    private val orderCcNumber = "5105105105105100"
    private val orderCcExpiration = "05/25"
    private val orderCcCvv = "123"

    @Autowired
    private lateinit var designRepository: TacoDesignRepository

    private val design1 by lazy {
        designRepository.save(TacoDesign(name = "my first taco", ingredients = getIngredients("COTO", "CARN")))
    }

    private val design2 by lazy {
        designRepository.save(TacoDesign(name = "my second taco", ingredients = getIngredients("FLTO", "GRBF")))
    }

    fun assertSameTacoDesigns(expected: List<TacoDesign>, actual: List<TacoDesign>, description: String) {
        // Compare IDs only - we don't need to go into a detailed comparison,
        // not least because it seems like small diffs in dates may throw off the test
        assertEquals(expected.map{it.id}, actual.map{it.id}, "$description - Taco design list not as expected")
    }

    @Test
    fun canSaveNewOrder() {
        val tacoDesigns = mutableListOf(design1, design2)

        val originalOrder = Order(name = orderName, street = orderStreet, city = orderCity, state = orderState,
            zip = orderZip, ccNumber = orderCcNumber, ccExpiration = orderCcExpiration, ccCvv = orderCcCvv,
            tacoDesigns = tacoDesigns
        )
        val savedOrder = repository.save(originalOrder)

        fun assertExpectedSavedOrder(actualOrder: Order, description: String) {
            assertEquals(orderName, actualOrder.name, "$description - Delivery name not as expected")
            assertEquals(orderStreet, actualOrder.street, "$description - Delivery street not as expected")
            assertEquals(orderCity, actualOrder.city, "$description - Delivery city not as expected")
            assertEquals(orderState, actualOrder.state, "$description - Delivery state not as expected")
            assertEquals(orderZip, actualOrder.zip, "$description - Delivery zip not as expected")
            assertEquals(orderCcNumber, actualOrder.ccNumber, "$description - CC number not as expected")
            assertEquals(orderCcExpiration, actualOrder.ccExpiration, "$description - CC expiration not as expected")
            assertEquals(orderCcCvv, actualOrder.ccCvv, "$description - CC CVV not as expected")
            assertSameTacoDesigns(tacoDesigns, actualOrder.tacoDesigns, description)
            assertDateSetRecently(actualOrder.createdDate, description, "Created date")
            assertDateSetRecently(actualOrder.updatedDate, description, "Updated date")
        }

        assertExpectedSavedOrder(savedOrder, "Saved order")
        assertExpectedSavedOrder(repository.findById(savedOrder.id!!).get(), "Retrieved order")
    }

    @Test
    fun canUpdateExistingOrder() {
        // Set up preexisting record
        val order = Order(name = orderName, street = orderStreet, city = orderCity, state = orderState,
            zip = orderZip, ccNumber = orderCcNumber, ccExpiration = orderCcExpiration, ccCvv = orderCcCvv,
            tacoDesigns = mutableListOf(design1, design2)
        )

        val originalSavedOrder = repository.save(order)
        val orderId = originalSavedOrder.id?: fail("Test should get a returned ID")
        val originalCreatedDate = originalSavedOrder.createdDate
        val originalUpdatedDate = originalSavedOrder.updatedDate

        // Sleep for one second so can be sure that last modified date will change
        Thread.sleep(1000)

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
            zip = orderZip, ccNumber = orderCcNumber, ccExpiration = orderCcExpiration, ccCvv = orderCcCvv,
            tacoDesigns = newDesigns
        )
        val updatedSavedOrder = repository.save(updatedOrder)

        fun assertExpectedUpdatedOrder(
            actualOrder: Order,
            description: String,
            // When updated object is returned from save method the created date will be null
            // (see https://github.com/spring-projects/spring-data-jpa/issues/1735)
            // so need to expect that where appropriate
            expectedCreatedDate: Date?
        ) {
            assertEquals(newName, actualOrder.name, "$description - Name not as expected")
            assertEquals(orderStreet, actualOrder.street, "$description - Street not as expected")
            assertEquals(orderCity, actualOrder.city, "$description - City not as expected")
            assertEquals(orderState, actualOrder.state, "$description - State not as expected")
            assertEquals(orderZip, actualOrder.zip, "$description - Zip not as expected")
            assertEquals(orderCcNumber, actualOrder.ccNumber, "$description - CC number not as expected")
            assertEquals(orderCcExpiration, actualOrder.ccExpiration, "$description - CC expiration not as expected")
            assertEquals(orderCcCvv, actualOrder.ccCvv, "$description - CC CVV not as expected")
            assertSameTacoDesigns(newDesigns, actualOrder.tacoDesigns, description)
            assertEquals(expectedCreatedDate, actualOrder.createdDate, "$description - Created date not as expected")
            assertDateSetRecently(actualOrder.updatedDate, description, "Updated date")
            assertNotEquals(originalUpdatedDate, actualOrder.updatedDate, "$description - Updated date not changed")
        }

        assertExpectedUpdatedOrder(updatedSavedOrder, "Saved order", null)
        assertExpectedUpdatedOrder(repository.findById(orderId).get(), "Retrieved order", originalCreatedDate)
    }

}
