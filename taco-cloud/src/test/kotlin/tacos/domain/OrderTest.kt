package tacos.domain

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import javax.validation.Validation

const val NAME_FOR_ORDER = "Armando"
const val STREET = "Blvd Justo Sierra"
const val CITY = "Hermosillo"
const val STATE = "Sonora"
const val ZIP = "83150"
const val CC_NUMBER = "378282246310005"
const val CC_EXPIRATION = "07/24"
const val CC_CVV = "123"

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OrderTest {

    private val validator = run { Validation.buildDefaultValidatorFactory().validator }

    fun incompleteOrders(): Stream<Arguments> {
        return Stream.of(
            Arguments.of(
                Order("", STREET, CITY, STATE, ZIP, CC_NUMBER, CC_EXPIRATION, CC_CVV),
                "Name is required"
            ),
            Arguments.of(
                Order(NAME_FOR_ORDER, "", CITY, STATE, ZIP, CC_NUMBER, CC_EXPIRATION, CC_CVV),
                "Street is required"
            ),
            Arguments.of(
                Order(NAME_FOR_ORDER, STREET, "", STATE, ZIP, CC_NUMBER, CC_EXPIRATION, CC_CVV),
                "City is required"
            ),
            Arguments.of(
                Order(NAME_FOR_ORDER, STREET, CITY, "", ZIP, CC_NUMBER, CC_EXPIRATION, CC_CVV),
                "State is required"
            ),
            Arguments.of(
                Order(NAME_FOR_ORDER, STREET, CITY, STATE, "", CC_NUMBER, CC_EXPIRATION, CC_CVV),
                "Zip code is required"
            ),
            Arguments.of(
                Order(NAME_FOR_ORDER, STREET, CITY, STATE, ZIP, "", CC_EXPIRATION, CC_CVV),
                "Not a valid credit card number"
            ),
            Arguments.of(
                Order(NAME_FOR_ORDER, STREET, CITY, STATE, ZIP, CC_NUMBER, "", CC_CVV),
                "Credit card expiration date must be formatted MM/YY"
            ),
            Arguments.of(
                Order(NAME_FOR_ORDER, STREET, CITY, STATE, ZIP, CC_NUMBER, CC_EXPIRATION, ""),
                "Invalid CVV"
            )
        )
    }

    @ParameterizedTest
    @MethodSource("incompleteOrders")
    fun `Validation enforces that all fields are set`(order: Order, expectedErrorMessage: String) {
        val violations = validator.validate(order)
        assertEquals(expectedErrorMessage, violations.single().message)
    }

}
