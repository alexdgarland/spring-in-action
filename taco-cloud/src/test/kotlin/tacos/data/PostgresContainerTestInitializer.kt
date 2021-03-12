package tacos.data

import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext

class PostgresContainerTestInitializer: ApplicationContextInitializer<ConfigurableApplicationContext> {

    override fun initialize(appContext: ConfigurableApplicationContext) {
        val postgresContainer: KPostgreSQLTestContainer = KPostgreSQLTestContainer.create()
        postgresContainer.start()

        TestPropertyValues.of(
            "spring.datasource.url=${postgresContainer.jdbcUrl}",
            "spring.datasource.username=${postgresContainer.username}",
            "spring.datasource.password=${postgresContainer.password}",
            "spring.jpa.properties.hibernate.enable_lazy_load_no_trans=true"
        ).applyTo(appContext)
    }

}
