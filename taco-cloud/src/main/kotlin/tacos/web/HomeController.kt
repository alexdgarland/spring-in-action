package tacos.web

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
@Suppress("FunctionOnlyReturningConstant")
class HomeController {

    private val logger = LoggerFactory.getLogger(javaClass)

    @GetMapping
    fun home(): String {
        logger.info("Loading home page")
        return "home"
    }

}
