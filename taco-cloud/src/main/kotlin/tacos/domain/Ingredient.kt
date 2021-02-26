package tacos.domain

import javax.persistence.*

enum class IngredientType { WRAP, PROTEIN, VEGGIES, CHEESE, SAUCE }

@Entity
data class Ingredient(
    @Id
    @Column(name = "ingredient_id")
    var id: String = "",

    @Column(name = "ingredient_name")
    var name: String = "",

    @Column(name = "ingredient_type")
    @Enumerated(EnumType.STRING)
    var type: IngredientType? = null
) {
    val description
    get() = "$id - $name ($type)"
}

data class IngredientCheckBoxViewModel(val id: String, val name: String, val checked: Boolean)
