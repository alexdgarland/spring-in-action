package tacos.domain

import org.hibernate.validator.constraints.CreditCardNumber
import org.springframework.stereotype.Component
import java.util.*
import javax.validation.constraints.Digits
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

@Component
data class Order(
    val id: Long? = null,
    @get:NotBlank(message="Name is required")
    @get:Size(max=50, message="Name cannot be more than 50 characters")
    var name: String="",
    @get:NotBlank(message="Street is required")
    @get:Size(max=50, message="Street cannot be more than 50 characters")
    var street: String="",
    @get:NotBlank(message="City is required")
    @get:Size(max=50, message="City cannot be more than 50 characters")
    var city: String="",
    @get:Size(min=2, max=2, message="State must be exactly two characters")
    var state: String="",
    @get:NotBlank(message="Zip code is required")
    @get:Size(max=10, message="Zip code cannot be more than 10 characters")
    var zip: String="",
    @get:CreditCardNumber(message="Not a valid credit card number")
    var ccNumber: String="",
    @get:Pattern(regexp="^(0[1-9]|1[0-2])([\\/])([1-9][0-9])$",
        message="Credit card expiration date must be formatted MM/YY")
    var ccExpiration: String="",
    @get:Digits(integer=3, fraction=0, message="Invalid CVV")
    var ccCvv: String="",
    val tacoDesigns: MutableList<TacoDesign> = mutableListOf(),
    val placedDate: Date? = null,
    val updatedDate: Date? = null
)
