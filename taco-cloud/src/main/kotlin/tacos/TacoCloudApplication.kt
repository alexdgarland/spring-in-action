package tacos

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
@ComponentScan
@EnableJpaRepositories("tacos.data.impl.postgresql")
class TacoCloudApplication

fun main(args: Array<String>) {

	@Suppress("SpreadOperator")
	runApplication<TacoCloudApplication>(*args)

}
