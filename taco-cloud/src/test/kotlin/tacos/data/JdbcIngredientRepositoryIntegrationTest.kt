package tacos.data

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.postgresql.ds.PGSimpleDataSource
import org.springframework.jdbc.core.JdbcTemplate
import org.testcontainers.containers.BindMode
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import tacos.domain.Ingredient
import tacos.domain.IngredientType

// TODO - maybe separate integration tests from unit test execution using Failsafe plugin or similar

// There are some examples here https://stackoverflow.com/questions/53078306/populate-a-database-with-testcontainers-in-a-springboot-integration-test
// which include an option to fully integrate with Spring (rather than binding init script location) which is where we may want to end up eventually.

@Testcontainers
class JdbcIngredientRepositoryIntegrationTest {

    class KPostgreSQLContainer(imageName: String): PostgreSQLContainer<KPostgreSQLContainer>(imageName)

    @Container
    val postgresContainer = KPostgreSQLContainer("postgres:13.1")
        .withExposedPorts(5432)
        .withFileSystemBind(
            "src/test/resources/migrations/sqls",   // TODO - This could be replaced by a direct reference to production DDL
            "/docker-entrypoint-initdb.d",
            BindMode.READ_ONLY
        )

    fun getTemplate(postgreSQLContainer: KPostgreSQLContainer): JdbcTemplate {
        val dataSource = PGSimpleDataSource()
        dataSource.serverName = postgresContainer.getHost()
        dataSource.user = postgresContainer.getUsername()
        dataSource.password = postgresContainer.getPassword()
        dataSource.portNumber = postgresContainer.getMappedPort(5432)
        return JdbcTemplate(dataSource)
    }

    @Test
    fun canSaveAndRetrieveIngredient() {

        // Some debugging - doesn't need to stay in long-term
        fun runCommand(vararg cmds: String) {
            println("Executing \"$cmds\" in Postgres container")
            val result = postgresContainer.execInContainer(*cmds)
            println("STDOUT: ${result.stdout}")
            println("STDERR: ${result.stderr}")
        }
        runCommand("ls", "docker-entrypoint-initdb.d")
        runCommand("psql", "-U", postgresContainer.username, "-c", "\\dt")

        // The actual test:
        val template = getTemplate(postgresContainer)
        val repository = JdbcIngredientRepository(template)
        val originalIngredient = Ingredient("COTO", "Corn Tortilla", IngredientType.WRAP)

        val savedIngredient = repository.save(originalIngredient)
        assertEquals(originalIngredient, savedIngredient)

        val retrievedIngredient = repository.findOne("COTO")
        assertEquals(originalIngredient, retrievedIngredient)
    }

    // TODO - add a test where ID is missing in the in-memory ingredient and is set on upsert to database

}
