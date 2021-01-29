package tacos.domain

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

const val NAME_FOR_ORDER = "Armando"
const val STREET = "Blvd Justo Sierra"
const val CITY = "Hermosillo"
const val STATE = "Sonora"
const val ZIP = "83150"
const val CC_NUMBER = "1234567891011121"
const val CC_EXPIRATION_STRING = "2024-07-12"
const val CC_CVV = "123"

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OrderViewModelTest {

    @Test
    fun `Conversion to domain object succeeds when all fields are non-null`() {
        val viewModel = OrderViewModel(NAME_FOR_ORDER, STREET, CITY, STATE, ZIP, CC_NUMBER, CC_EXPIRATION_STRING, CC_CVV)
        val domainObject = viewModel.toOrder()
        assertEquals(NAME_FOR_ORDER, domainObject.name)
        assertEquals(STREET, domainObject.street)
        assertEquals(CITY, domainObject.city)
        assertEquals(STATE, domainObject.state)
        assertEquals(ZIP, domainObject.zip)
        assertEquals(CC_NUMBER, domainObject.ccNumber)
        assertEquals(CC_EXPIRATION_STRING, dateFormat.format(domainObject.ccExpiration))
        assertEquals(CC_CVV, domainObject.ccCvv)
    }

    fun incompleteViewModels(): Stream<Arguments> {
        return Stream.of(
            OrderViewModel(null, STREET, CITY, STATE, ZIP, CC_NUMBER, CC_EXPIRATION_STRING, CC_CVV),
            OrderViewModel(NAME_FOR_ORDER, null, CITY, STATE, ZIP, CC_NUMBER, CC_EXPIRATION_STRING, CC_CVV),
            OrderViewModel(NAME_FOR_ORDER, STREET, null, STATE, ZIP, CC_NUMBER, CC_EXPIRATION_STRING, CC_CVV),
            OrderViewModel(NAME_FOR_ORDER, STREET, CITY, null, ZIP, CC_NUMBER, CC_EXPIRATION_STRING, CC_CVV),
            OrderViewModel(NAME_FOR_ORDER, STREET, CITY, STATE, null, CC_NUMBER, CC_EXPIRATION_STRING, CC_CVV),
            OrderViewModel(NAME_FOR_ORDER, STREET, CITY, STATE, ZIP, null, CC_EXPIRATION_STRING, CC_CVV),
            OrderViewModel(NAME_FOR_ORDER, STREET, CITY, STATE, ZIP, CC_NUMBER, null, CC_CVV),
            OrderViewModel(NAME_FOR_ORDER, STREET, CITY, STATE, ZIP, CC_NUMBER, CC_EXPIRATION_STRING, null)
        ).map { Arguments.of(it) }
    }

    @ParameterizedTest
    @MethodSource("incompleteViewModels")
    fun `Conversion to domain object throws ViewModelValidationException when any field is null`(
        viewModel: OrderViewModel
    ) {
        assertThrows<ViewModelValidationException> { viewModel.toOrder() }
    }

    @Test
    fun `Conversion to domain object throws ViewModelValidationException when expiration date is in invalid format`() {
        val viewModel = OrderViewModel(
            NAME_FOR_ORDER, STREET, CITY, STATE, ZIP, CC_NUMBER, "07-12-2024", CC_CVV
        )
        assertThrows<ViewModelValidationException> { viewModel.toOrder() }
    }

}
