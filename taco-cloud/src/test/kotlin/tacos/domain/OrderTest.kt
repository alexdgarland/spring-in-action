package tacos.domain

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import javax.validation.Validation

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OrderTest {

    private val validator = run { Validation.buildDefaultValidatorFactory().validator }

    // Defaults are valid values; can pass other values to force an expected partial failure
    @Suppress("LongParameterList")
    fun createOrder(id: Long=1L, name: String="Armando",
        street: String="Blvd Justo Sierra", city: String="Hermosillo", state: String="SO", zip: String="83150",
        ccNumber: String="378282246310005", ccExpiration: String="07/24", ccCvv: String="123"
    ) = Order(id, name, street, city, state, zip, ccNumber, ccExpiration, ccCvv)

    fun incompleteOrders(): Stream<Arguments> {
        return Stream.of(
            Arguments.of(createOrder(name = ""), "Name is required"),
            Arguments.of(createOrder(street = ""), "Street is required"),
            Arguments.of(createOrder(city = ""), "City is required"),
            Arguments.of(createOrder(state = ""), "State must be exactly two characters"),
            Arguments.of(createOrder(zip = ""), "Zip code is required"),
            Arguments.of(createOrder(ccNumber = ""), "Not a valid credit card number"),
            Arguments.of(createOrder(ccNumber = "77777777"), "Not a valid credit card number"),
            Arguments.of(createOrder(ccExpiration = ""), "Credit card expiration date must be formatted MM/YY"),
            Arguments.of(createOrder(ccCvv = ""), "Invalid CVV")
        )
    }

    @ParameterizedTest
    @MethodSource("incompleteOrders")
    fun `Validation enforces that all fields are set`(order: Order, expectedErrorMessage: String) {
        val violations = validator.validate(order)
        assertEquals(expectedErrorMessage, violations.single().message)
    }

    fun ordersWithTooLongValues(): Stream<Arguments> {
        val fiftyFiveChars = "XXXX0XXXX0XXXX0XXXX0XXXX0XXXX0XXXX0XXXX0XXXX0XXXX0XXXX0"
        return Stream.of(
            Arguments.of(createOrder(name = fiftyFiveChars), "Name cannot be more than 50 characters"),
            Arguments.of(createOrder(street = fiftyFiveChars), "Street cannot be more than 50 characters"),
            Arguments.of(createOrder(city = fiftyFiveChars), "City cannot be more than 50 characters"),
            Arguments.of(createOrder(state = "A"), "State must be exactly two characters"),
            Arguments.of(createOrder(state = "AAA"), "State must be exactly two characters"),
            Arguments.of(createOrder(zip = "XXXX0XXXX0XXXX0"), "Zip code cannot be more than 10 characters")
        )
    }

    @ParameterizedTest
    @MethodSource("ordersWithTooLongValues")
    fun `Validation enforces that all fields will fit in database`(order: Order, expectedErrorMessage: String) {
        val violations = validator.validate(order)
        assertEquals(expectedErrorMessage, violations.single().message)
    }

}
