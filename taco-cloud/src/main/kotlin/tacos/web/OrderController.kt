package tacos.web

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.Errors
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.SessionAttributes
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import tacos.data.OrderRepository
import tacos.domain.Order
import javax.validation.Valid

@Controller
@SessionAttributes("order")
@RequestMapping("/orders")
class OrderController @Autowired constructor(val orderRepository: OrderRepository) {

    private val logger = LoggerFactory.getLogger(javaClass)

    @GetMapping("/current")
    fun orderForm(model: Model): String {
        logger.info("Loading order form page")
        return "orderForm"
    }

    @PostMapping
    fun processOrder(@Valid @ModelAttribute(name = "order") order: Order, errors: Errors): String {
        if (errors.hasErrors()) {
            return "orderForm"
        }
        orderRepository.save(order)
        logger.info("Order submitted: $order")
        return "redirect:/"
    }

}
