package tacos.web

import org.hamcrest.Matchers.containsString
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import tacos.data.IngredientRepository
import tacos.data.OrderRepository

@WebMvcTest
class HomeControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var jdbcTemplate: JdbcTemplate

    @MockBean
    private lateinit var postgresqlOrderRepository: OrderRepository

    @MockBean
    private lateinit var postgresqlIngredientRepository: IngredientRepository


    @Test
    fun testHomePage() {
        mockMvc
            .perform(get("/"))
            .andExpect(status().isOk)
            .andExpect(view().name("home"))
            .andExpect(content().string(containsString("Bienvenido a la nube de tacos...")))
    }

}
