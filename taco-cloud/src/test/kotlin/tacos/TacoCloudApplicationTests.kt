package tacos

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.test.context.ContextConfiguration
import tacos.data.KPostgreSQLTestContainer

class PostgresContainerInitializer: ApplicationContextInitializer<ConfigurableApplicationContext> {

	override fun initialize(context: ConfigurableApplicationContext) {
		val container = KPostgreSQLTestContainer.create()
		container.start()
		TestPropertyValues.of(
			"spring.datasource.url=${container.jdbcUrl}",
			"spring.datasource.username=${container.username}",
			"spring.datasource.password=${container.password}"
		).applyTo(context)
	}

}

@SpringBootTest
@ContextConfiguration(initializers = [PostgresContainerInitializer::class])
class TacoCloudApplicationTests {

	@Test
	@Suppress("EmptyFunctionBlock")
	fun contextLoads() {
	}

}
