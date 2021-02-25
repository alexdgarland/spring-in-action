package tacos

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan
class TacoCloudApplication

fun main(args: Array<String>) {

	@Suppress("SpreadOperator")
	runApplication<TacoCloudApplication>(*args)

}
