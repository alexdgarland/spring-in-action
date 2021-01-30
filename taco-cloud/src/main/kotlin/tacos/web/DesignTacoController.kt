package tacos.web

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import tacos.domain.Ingredient
import tacos.domain.IngredientType
import tacos.domain.TacoDesignViewModel

val availableIngredients = listOf(
    Ingredient("FLTO", "Flour Tortilla", IngredientType.WRAP),
    Ingredient("COTO", "Corn Tortilla", IngredientType.WRAP),
    Ingredient("GRBF", "Ground Beef", IngredientType.PROTEIN),
    Ingredient("CARN", "Carnitas", IngredientType.PROTEIN),
    Ingredient("TMTO", "Diced Tomatoes", IngredientType.VEGGIES),
    Ingredient("LETC", "Lettuce", IngredientType.VEGGIES),
    Ingredient("CHED", "Cheddar", IngredientType.CHEESE),
    Ingredient("JACK", "Monterrey Jack", IngredientType.CHEESE),
    Ingredient("SLSA", "Salsa", IngredientType.SAUCE),
    Ingredient("SRCR", "Sour Cream", IngredientType.SAUCE)
)

@Controller
@RequestMapping("/design")
class DesignTacoController {

    private val logger = LoggerFactory.getLogger(javaClass)

    @GetMapping
    fun showDesignForm(model: Model): String {
        logger.info("Loading taco design page")

        IngredientType.values().forEach { type ->
            model.addAttribute(type.toString().toLowerCase(), availableIngredients.filter { it.type == type })
        }
        model.addAttribute("design", TacoDesignViewModel())

        return "design"
    }

    @PostMapping
    fun processDesign(designViewModel: TacoDesignViewModel): String {
        logger.info("Processing design: ${designViewModel.toTacoDesign()}")
        return "redirect:/orders/current"
    }


}
