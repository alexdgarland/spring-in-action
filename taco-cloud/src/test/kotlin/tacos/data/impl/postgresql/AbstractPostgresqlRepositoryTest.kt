package tacos.data.impl.postgresql

import org.postgresql.ds.PGSimpleDataSource
import org.springframework.jdbc.core.JdbcTemplate
import org.testcontainers.junit.jupiter.Container
import tacos.data.KPostgreSQLTestContainer

// TODO - maybe separate integration tests from unit test execution using Failsafe plugin or similar

// TODO - remove this suppression once the abstract class is used in multiple tests
@Suppress("UnnecessaryAbstractClass")
abstract class AbstractPostgresqlRepositoryTest {

    @Container
    val postgresContainer: KPostgreSQLTestContainer = KPostgreSQLTestContainer.create()
        .withInitScript("schema.sql", "01-schema.sql")
        .withInitScript("data.sql", "02-data.sql")

    val template: JdbcTemplate
    get() {
        val dataSource = PGSimpleDataSource()
        dataSource.serverName = postgresContainer.getHost()
        dataSource.user = postgresContainer.getUsername()
        dataSource.password = postgresContainer.getPassword()
        dataSource.portNumber = postgresContainer.getMappedPort(5432)
        return JdbcTemplate(dataSource)
    }

}
