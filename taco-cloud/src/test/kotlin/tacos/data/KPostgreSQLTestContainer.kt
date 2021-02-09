package tacos.data

import org.testcontainers.containers.BindMode
import org.testcontainers.containers.PostgreSQLContainer

class KPostgreSQLTestContainer private constructor(dockerImageName: String, val port: Int):
    PostgreSQLContainer<KPostgreSQLTestContainer>(dockerImageName)
{

    fun withInitScript(source: String, target: String): KPostgreSQLTestContainer {
        return this.withFileSystemBind(
            "src/main/resources/$source",
            "/docker-entrypoint-initdb.d/$target",
            BindMode.READ_ONLY
        )
    }

    val mappedPort: Int
    get() = getMappedPort(port)

    companion object {

        fun create(dockerImageName: String = "postgres:13.1", port: Int = 5432): KPostgreSQLTestContainer {
            return KPostgreSQLTestContainer(dockerImageName, port).withExposedPorts(port)
        }

    }

}
