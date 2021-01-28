package tacos

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
@Suppress("FunctionOnlyReturningConstant")
class HomeController {

    @GetMapping
    fun home() = "home"

}
