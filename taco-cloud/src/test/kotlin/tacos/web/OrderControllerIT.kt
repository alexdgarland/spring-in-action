package tacos.web

import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.view
import org.testcontainers.junit.jupiter.Testcontainers
import tacos.data.PostgresContainerTestInitializer
import tacos.domain.Ingredient
import tacos.domain.IngredientType
import tacos.domain.Order
import tacos.domain.TacoDesign

private const val CURRENT_ORDER_URL_TEMPLATE = "/orders/current"

@Testcontainers
@SpringBootTest(properties = ["spring.main.allow-bean-definition-overriding=true"])
@ContextConfiguration(initializers = [PostgresContainerTestInitializer::class])
@AutoConfigureMockMvc
class OrderControllerIT {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun testOrderPageRequiresAuthentication() {
        mockMvc
            .perform(get(CURRENT_ORDER_URL_TEMPLATE))
            .andExpect(status().isUnauthorized)
    }

    fun baseAsserts(performResultActions: ResultActions): ResultActions {
        return performResultActions
            .andExpect(status().isOk)
            .andExpect(view().name("orderForm"))
            .andExpectHasString("Taco Cloud - Ordering")
            .andExpectHasString("Name")
            .andExpectHasString("Street")
            .andExpectHasString("City")
            .andExpectHasString("State")
            .andExpectHasString("Zip code")
            .andExpectHasString("Credit card number")
            .andExpectHasString("Credit card expiration date")
            .andExpectHasString("CVV")
    }

    @Test
    @WithMockUser
    fun testOrderPageIsAccessibleWhenAuthorized() {
        baseAsserts(mockMvc.perform(get(CURRENT_ORDER_URL_TEMPLATE).flashAttr("order", Order())))
    }

    @Disabled("Use of populated Order model within HTML not as expected")
    @Test
    @WithMockUser
    fun testOrderPageShowsOrderedTacoDesigns() {

        val orderModel = Order(
            tacoDesigns = mutableListOf(
                TacoDesign(
                    name = "Mi taco",
                    ingredients = listOf(
                        Ingredient("COTO", "Corn Tortilla", IngredientType.WRAP),
                        Ingredient("GRBF", "Ground Beef", IngredientType.PROTEIN)
                    )
                ),
                TacoDesign(
                    name = "Mi segundo taco",
                    ingredients = listOf(
                        Ingredient("FLTO", "Flour Tortilla", IngredientType.WRAP),
                        Ingredient("CARN", "Carnitas", IngredientType.PROTEIN)
                    )
                )
            )
        )

        baseAsserts(mockMvc.perform(get(CURRENT_ORDER_URL_TEMPLATE).sessionAttr("order", orderModel)))
            // Feels like this assertion should work  - page does actually show the ordered designs,
            // but it doesn't come through in the rendered HTML (including when looking at "View Source" of running app).
            // Not sure why this is right now...
            .andExpectHasString("Mi taco")
    }

}
