package tacos.web

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.testcontainers.junit.jupiter.Testcontainers
import tacos.data.PostgresContainerTestInitializer

private const val REGISTER_URL_TEMPLATE = "/register"

@Testcontainers
@SpringBootTest(properties = ["spring.main.allow-bean-definition-overriding=true"])
@ContextConfiguration(initializers = [PostgresContainerTestInitializer::class])
@AutoConfigureMockMvc
class RegistrationControllerIT {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun testHomePageIsAccessibleWithoutAuthorization() {
        mockMvc
            .perform(MockMvcRequestBuilders.get(REGISTER_URL_TEMPLATE))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.view().name("registrationForm"))
            .andExpectHasString("Register")
            .andExpectHasString("Username")
            .andExpectHasString("Password")
            .andExpectHasString("Confirm password")
            .andExpectHasString("Full name")
            .andExpectHasString("Street")
            .andExpectHasString("City")
            .andExpectHasString("State")
            .andExpectHasString("Zip")
            .andExpectHasString("Phone")
    }

}
