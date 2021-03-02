package tacos.data

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.test.context.ContextConfiguration
import org.testcontainers.junit.jupiter.Testcontainers
import tacos.domain.Order
import tacos.domain.TacoDesign
import tacos.domain.getIngredients
import java.text.SimpleDateFormat
import javax.transaction.Transactional

@Testcontainers
@SpringBootTest(properties = ["spring.main.allow-bean-definition-overriding=true"])
@ContextConfiguration(initializers = [PostgresContainerTestInitializer::class])
@Transactional
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

    private val testDate = SimpleDateFormat("yyyy-MM-dd").parse("2021-01-24")

    @Autowired
    private lateinit var designRepository: TacoDesignRepository

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
            zip = orderZip, ccNumber = orderCcNumber, ccExpiration = orderCcExpiration, ccCvv = orderCcCvv,
            tacoDesigns = tacoDesigns, placedDate = null
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
            assertEquals(tacoDesigns, actualOrder.tacoDesigns, "$description - Taco designs not as expected")
            assertDateSetRecently(actualOrder.placedDate, description, "Placed date")
        }

        assertExpectedSavedOrder(savedOrder, "Saved order")
        assertExpectedSavedOrder(repository.findById(savedOrder.id!!).get(), "Retrieved order")
    }

//    @Test
//    fun canUpdateExistingOrder() {
//        // Set up preexisting record
//        val originalDate = SimpleDateFormat("yyyy-MM-dd").parse("2021-01-23")
//        val originalOrder = Order(name = orderName, street = orderStreet, city = orderCity, state = orderState,
//            zip = orderZip, ccNumber = orderCcNumber, ccExpiration = orderCcExpiration, ccCvv = orderCcv,
//            tacoDesigns = mutableListOf(design1, design2), placedDate=originalDate, updatedDate=originalDate
//        )
//        val orderId = repository.save(originalOrder).id?: fail("Test should get a returned ID")
//
//        // Run an update
//        val newName = "New and improved!"
//        val design3 = designRepository.save(
//            TacoDesign(
//                name = "my third taco",
//                ingredients = getIngredients("COTO", "GRBF", "SLSA")
//            )
//        )
//        val newDesigns = mutableListOf(design1, design3)
//        val updatedOrder = Order(id = orderId, name = newName, street = orderStreet, city = orderCity, state = orderState,
//            zip = orderZip, ccNumber = orderCcNumber, ccExpiration = orderCcExpiration, ccCvv = orderCcv,
//            tacoDesigns = newDesigns, placedDate=originalDate, updatedDate=originalDate
//        )
//        val savedOrder = repository.save(updatedOrder)
//
//        // Assert
//        val expectedSavedOrder = Order(id = orderId, name = newName, street = orderStreet, city = orderCity, state = orderState,
//            zip = orderZip, ccNumber = orderCcNumber, ccExpiration = orderCcExpiration, ccCvv = orderCcv,
//            tacoDesigns = newDesigns, placedDate=originalDate, updatedDate=testDate
//        )
//        assertEquals(expectedSavedOrder, savedOrder)
//        assertEquals(expectedSavedOrder, repository.findById(orderId).get())
//    }

}
