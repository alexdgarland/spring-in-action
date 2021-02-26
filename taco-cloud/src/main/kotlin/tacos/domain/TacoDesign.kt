package tacos.domain

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.util.*
import javax.persistence.*
import javax.validation.constraints.Size

@Entity
@EntityListeners(AuditingEntityListener::class)
data class TacoDesign(
    @Id
    @Column(name = "taco_design_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "taco_design_name")
    @get:Size(min=5, message = "Name must be at least 5 characters long")
    var name: String = "",

    @ManyToMany(targetEntity = Ingredient::class)
    @JoinTable(
        name = "taco_design_ingredients",
        joinColumns = [JoinColumn(name ="taco_design_id")],
        inverseJoinColumns = [JoinColumn(name = "ingredient_id")]
    )
    @get:Size(min=1, message = "You must choose at least one ingredient")
    var ingredients: List<Ingredient> = emptyList(),

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreatedDate
    var createdDate: Date? = null,
) {

    fun getIngredientUiMap(
        availableIngredients: Iterable<Ingredient>
    ): Map<String, List<IngredientCheckBoxViewModel>> {
        return availableIngredients
            .groupBy { ingredient -> ingredient.type.toString().toLowerCase() }
            .mapValues { entry ->
                entry.value.map { availableIngredient ->
                    IngredientCheckBoxViewModel(
                        id = availableIngredient.id,
                        name = availableIngredient.name,
                        checked = ingredients.map{it.id}.contains(availableIngredient.id)
                    )
                }
            }
    }

}
