package tacos.domain

import java.util.*
import javax.validation.constraints.Size

data class TacoDesign(
    var id: Long? = null,
    @get:Size(min=5, message = "Name must be at least 5 characters long")
    var name: String = "",
    @get:Size(min=1, message = "You must choose at least one ingredient")
    var ingredients: List<String> = emptyList(),
    val createdDate: Date? = null,
    val updatedDate: Date? = null
)
