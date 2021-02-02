package tacos.web

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.Errors
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import tacos.domain.OrderViewModel
import javax.validation.Valid

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
    fun processOrder(@Valid @ModelAttribute("order") orderViewModel: OrderViewModel, errors: Errors): String {
        if (errors.hasErrors()) {
            return "orderForm"
        }
        logger.info("Order submitted: ${orderViewModel.toOrder()}")
        return "redirect:/"
    }

}
