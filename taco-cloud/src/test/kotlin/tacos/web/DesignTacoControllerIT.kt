package tacos.web

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

private const val DESIGN_URL_TEMPLATE = "/design"

@Testcontainers
@SpringBootTest(properties = ["spring.main.allow-bean-definition-overriding=true"])
@ContextConfiguration(initializers = [PostgresContainerTestInitializer::class])
@AutoConfigureMockMvc
class DesignTacoControllerIT {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun testTacoDesignPageRequiresAuthentication() {
        mockMvc
            .perform(get(DESIGN_URL_TEMPLATE))
            .andExpect(status().isForbidden)
    }

    @Test
    @WithMockUser
    fun testTacoDesignPageIsAccessibleWhenAuthorized() {

        fun ResultActions.andExpectHeaderFor(ingredientType: String) =
            andExpectHasString("Select your <span>$ingredientType")

        mockMvc
            .perform(get(DESIGN_URL_TEMPLATE))
            .andExpect(status().isOk)
            .andExpect(view().name("design"))
            .andExpectHasString("Design your taco!")
            .andExpectHeaderFor("wrap")
            .andExpectHeaderFor("protein")
            .andExpectHeaderFor("veggies")
            .andExpectHeaderFor("cheese")
            .andExpectHeaderFor("sauce")
    }

}
