package tacos.web

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import tacos.domain.OrderViewModel


@Controller
@RequestMapping("/orders")
class OrderController {

    private val logger = LoggerFactory.getLogger(javaClass)

    @GetMapping("/current")
    fun orderForm(model: Model): String {
        logger.info("Loading order form page")
        model.addAttribute("order", OrderViewModel())
        return "orderForm"
    }

    @PostMapping
    fun processOrder(orderViewModel: OrderViewModel): String {
        logger.info("Order submitted: ${orderViewModel.toOrder()}")
        return "redirect:/"
    }

}
