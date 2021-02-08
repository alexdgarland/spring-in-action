package tacos.data.impl.postgresql

import org.postgresql.ds.PGSimpleDataSource
import org.springframework.jdbc.core.JdbcTemplate
import org.testcontainers.containers.BindMode
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container

// TODO - maybe separate integration tests from unit test execution using Failsafe plugin or similar

// There are some examples here https://stackoverflow.com/questions/53078306/populate-a-database-with-testcontainers-in-a-springboot-integration-test
// which include an option to fully integrate with Spring (rather than binding init script location) which is where we may want to end up eventually.

class KPostgreSQLContainer(imageName: String): PostgreSQLContainer<KPostgreSQLContainer>(imageName)

abstract class AbstractPostgresqlRepositoryTest {

    @Container
    val postgresContainer = KPostgreSQLContainer("postgres:13.1")
        .withExposedPorts(5432)
        .withFileSystemBind(
            "src/main/resources/schema.sql",
            "/docker-entrypoint-initdb.d/01-schema.sql",
            BindMode.READ_ONLY
        )
        .withFileSystemBind(
            "src/main/resources/data.sql",
            "/docker-entrypoint-initdb.d/02-data.sql",
            BindMode.READ_ONLY
        )

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
