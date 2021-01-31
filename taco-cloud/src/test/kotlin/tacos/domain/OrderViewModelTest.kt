package tacos.domain

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
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
class OrderViewModelTest {

    private val validator = run { Validation.buildDefaultValidatorFactory().validator }

    fun incompleteViewModels(): Stream<Arguments> {
        return Stream.of(
            Arguments.of(
                OrderViewModel("", STREET, CITY, STATE, ZIP, CC_NUMBER, CC_EXPIRATION, CC_CVV),
                "Name is required"
            ),
            Arguments.of(
                OrderViewModel(NAME_FOR_ORDER, "", CITY, STATE, ZIP, CC_NUMBER, CC_EXPIRATION, CC_CVV),
                "Street is required"
            ),
            Arguments.of(
                OrderViewModel(NAME_FOR_ORDER, STREET, "", STATE, ZIP, CC_NUMBER, CC_EXPIRATION, CC_CVV),
                "City is required"
            ),
            Arguments.of(
                OrderViewModel(NAME_FOR_ORDER, STREET, CITY, "", ZIP, CC_NUMBER, CC_EXPIRATION, CC_CVV),
                "State is required"
            ),
            Arguments.of(
                OrderViewModel(NAME_FOR_ORDER, STREET, CITY, STATE, "", CC_NUMBER, CC_EXPIRATION, CC_CVV),
                "Zip code is required"
            ),
            Arguments.of(
                OrderViewModel(NAME_FOR_ORDER, STREET, CITY, STATE, ZIP, "", CC_EXPIRATION, CC_CVV),
                "Not a valid credit card number"
            ),
            Arguments.of(
                OrderViewModel(NAME_FOR_ORDER, STREET, CITY, STATE, ZIP, CC_NUMBER, "", CC_CVV),
                "Credit card expiration date must be formatted MM/YY"
            ),
            Arguments.of(
                OrderViewModel(NAME_FOR_ORDER, STREET, CITY, STATE, ZIP, CC_NUMBER, CC_EXPIRATION, ""),
                "Invalid CVV"
            )
        )
    }

    @ParameterizedTest
    @MethodSource("incompleteViewModels")
    fun `Validation enforces that all fields are set`(
        viewModel: OrderViewModel,
        expectedErrorMessage: String
    ) {
        val violations = validator.validate(viewModel)
        println(violations)
        assertEquals(expectedErrorMessage, violations.single().message)
    }

    @Test
    fun `Conversion to domain object succeeds when all fields are valid`() {
        val viewModel = OrderViewModel(NAME_FOR_ORDER, STREET, CITY, STATE, ZIP, CC_NUMBER, CC_EXPIRATION, CC_CVV)
        val domainObject = viewModel.toOrder()
        assertEquals(NAME_FOR_ORDER, domainObject.name)
        assertEquals(STREET, domainObject.street)
        assertEquals(CITY, domainObject.city)
        assertEquals(STATE, domainObject.state)
        assertEquals(ZIP, domainObject.zip)
        assertEquals(CC_NUMBER, domainObject.ccNumber)
        assertEquals(CC_EXPIRATION, domainObject.ccExpiration)
        assertEquals(CC_CVV, domainObject.ccCvv)
    }

}
