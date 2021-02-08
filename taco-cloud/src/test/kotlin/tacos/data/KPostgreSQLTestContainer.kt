package tacos.data

import org.testcontainers.containers.BindMode
import org.testcontainers.containers.PostgreSQLContainer

class KPostgreSQLTestContainer(dockerImageName: String): PostgreSQLContainer<KPostgreSQLTestContainer>(dockerImageName) {

    fun withInitScript(source: String, target: String): KPostgreSQLTestContainer {
        return this.withFileSystemBind(
            "src/main/resources/$source",
            "/docker-entrypoint-initdb.d/$target",
            BindMode.READ_ONLY
        )
    }

    companion object {

        fun create(dockerImageName: String = "postgres:13.1", port: Int = 5432): KPostgreSQLTestContainer {
            return KPostgreSQLTestContainer(dockerImageName).withExposedPorts(port)
        }

    }

}
