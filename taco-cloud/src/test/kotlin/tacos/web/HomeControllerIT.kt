package tacos.web

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.view
import org.testcontainers.junit.jupiter.Testcontainers
import tacos.data.PostgresContainerTestInitializer

private const val HOME_URL_TEMPLATE = "/"

@Testcontainers
@SpringBootTest(properties = ["spring.main.allow-bean-definition-overriding=true"])
@ContextConfiguration(initializers = [PostgresContainerTestInitializer::class])
@AutoConfigureMockMvc
class HomeControllerIT {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun testHomePageRequiresAuthentication() {
        mockMvc
            .perform(get(HOME_URL_TEMPLATE))
            .andExpect(status().isUnauthorized)
    }

    @Test
    @WithMockUser
    fun testHomePageIsAccessibleWhenAuthorized() {
        mockMvc
            .perform(get(HOME_URL_TEMPLATE))
            .andExpect(status().isOk)
            .andExpect(view().name("home"))
            .andExpectHasString("Bienvenido a la nube de tacos...")
    }

}
