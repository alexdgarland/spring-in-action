package tacos.web

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.Errors
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import tacos.data.IngredientRepository
import tacos.domain.Ingredient
import tacos.domain.IngredientCheckBoxViewModel
import tacos.domain.TacoDesign
import javax.validation.Valid

// Build lists of ingredients, including preserving of state of checkboxes when validation errors are thrown.
// Not sure if under any circumstances this would be better done in JavaScript?
fun getIngredientUiMap(
    availableIngredients: Iterable<Ingredient>,
    design: TacoDesign
): Map<String, List<IngredientCheckBoxViewModel>> {
    return availableIngredients
        .groupBy { ingredient -> ingredient.type.toString().toLowerCase() }
        .mapValues { entry ->
            entry.value.map { ingredient ->
                IngredientCheckBoxViewModel(
                    id = ingredient.id,
                    name = ingredient.name,
                    checked = design.ingredients.contains(ingredient.id))
            }
        }
}

@Controller
@RequestMapping("/design")
class DesignTacoController(@Autowired val ingredientRepository: IngredientRepository) {

    private val logger = LoggerFactory.getLogger(javaClass)

    fun setUpDesignPage(uiModel: Model, design: TacoDesign): String {
        logger.info("Loading taco design page")
        val availableIngredients = ingredientRepository.findAll()
        val ingredientUiMap = getIngredientUiMap(availableIngredients, design)
        uiModel.addAttribute("ingredientMap", ingredientUiMap)
        uiModel.addAttribute("design", design)
        return "design"
    }

    @GetMapping
    fun showDesignForm(uiModel: Model): String = setUpDesignPage(uiModel, TacoDesign())

    @PostMapping
    fun processDesign(
        uiModel: Model,
        @Valid @ModelAttribute("design") design: TacoDesign,
        errors: Errors
    ): String {
        if (errors.hasErrors()) {
            return setUpDesignPage(uiModel, design)
        }
        logger.info("Processing design \"${design.name}\"")
        logger.info("Ingredients:\n${design.ingredients.joinToString("\n")}")
        return "redirect:/orders/current"
    }

}
