package tacos.data.impl.postgresql

import org.postgresql.ds.PGSimpleDataSource
import org.springframework.jdbc.core.JdbcTemplate
import org.testcontainers.junit.jupiter.Container
import tacos.data.KPostgreSQLTestContainer

open class PostgresqlIntegrationTestWithContainer {

    @Container
    val postgresContainer: KPostgreSQLTestContainer = KPostgreSQLTestContainer.create()
        .withInitScript("schema.sql", "01-schema.sql")
        .withInitScript("data.sql", "02-data.sql")

    val template: JdbcTemplate
    get() {
        val dataSource = PGSimpleDataSource()
        dataSource.serverName = postgresContainer.host
        dataSource.user = postgresContainer.username
        dataSource.password = postgresContainer.password
        dataSource.portNumber = postgresContainer.mappedPort
        return JdbcTemplate(dataSource)
    }

}