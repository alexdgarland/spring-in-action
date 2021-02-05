package tacos.domain

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import javax.validation.Validation

const val DESIGN_NAME = "Mi taco"
val ingredientList = listOf("COTO", "GRBF", "JACK", "TMTO", "LETC", "SLSA")

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TacoDesignTest {

    private val validator = run { Validation.buildDefaultValidatorFactory().validator }

    fun incompleteDesigns(): Stream<Arguments> {
        return Stream.of(
            Arguments.of(TacoDesign(ingredients = ingredientList), "Name must be at least 5 characters long"),
            Arguments.of(TacoDesign(name = DESIGN_NAME), "You must choose at least one ingredient"),
        )
    }

    @ParameterizedTest
    @MethodSource("incompleteDesigns")
    fun `Validation enforces that all fields are set`(design: TacoDesign, expectedErrorMessage: String) {
        val violations = validator.validate(design)
        assertEquals(expectedErrorMessage, violations.single().message)
    }

}
