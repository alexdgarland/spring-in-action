package tacos.web

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.Errors
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import tacos.data.IngredientRepository
import tacos.data.TacoDesignRepository
import tacos.domain.Order
import tacos.domain.TacoDesign
import javax.validation.Valid

@Controller
@SessionAttributes("order")
@RequestMapping("/design")
class DesignTacoController @Autowired constructor(
    val ingredientRepository: IngredientRepository,
    val tacoDesignRepository: TacoDesignRepository,
    @ModelAttribute(name = "order") val order: Order
    ) {

    private val logger = LoggerFactory.getLogger(javaClass)

    fun setUpDesignPage(uiModel: Model, design: TacoDesign): String {
        logger.info("Loading taco design page")

        val availableIngredients = ingredientRepository.findAll()
        val ingredientUiMap = design.getIngredientUiMap(availableIngredients)

        uiModel.addAttribute("ingredientMap", ingredientUiMap)
        uiModel.addAttribute("design", design)

        return "design"
    }

    @GetMapping
    fun showDesignForm(uiModel: Model): String = setUpDesignPage(uiModel, TacoDesign())

    @PostMapping
    fun processDesign(
        uiModel: Model,
        @Valid @ModelAttribute(name = "design") design: TacoDesign,
        redirectAttributes: RedirectAttributes,
        errors: Errors
    ): String {
        if (errors.hasErrors()) {
            return setUpDesignPage(uiModel, design)
        }

        logger.info("Processing design \"${design.name}\" (ingredients: ${design.ingredients})")

        val savedDesign = tacoDesignRepository.save(design)
        order.tacoDesigns.add(savedDesign)

        logger.info("Current state of order is: ${order.toString()}")
        uiModel.addAttribute("order", order)

        val designs = order.getDesignsWithIngredientDescription(ingredientRepository)
        logger.info("Designs in order: $designs")
        redirectAttributes.addFlashAttribute("designs", designs)

        return "redirect:/orders/current"
    }

}
