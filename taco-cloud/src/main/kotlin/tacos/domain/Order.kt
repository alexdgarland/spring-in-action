package tacos.domain

import org.hibernate.validator.constraints.CreditCardNumber
import javax.validation.constraints.Digits
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern

data class Order(
    val name: String,
    val street: String,
    val city: String,
    val state: String,
    val zip: String,
    val ccNumber: String,
    val ccExpiration: String,
    val ccCvv: String
)

data class OrderViewModel(
    @get:NotBlank(message="Name is required")
    var name: String="",
    @get:NotBlank(message="Street is required")
    var street: String="",
    @get:NotBlank(message="City is required")
    var city: String="",
    @get:NotBlank(message="State is required")
    var state: String="",
    @get:NotBlank(message="Zip code is required")
    var zip: String="",
    @get:CreditCardNumber(message="Not a valid credit card number")
    var ccNumber: String="",
    @get:Pattern(regexp="^(0[1-9]|1[0-2])([\\/])([1-9][0-9])$",
        message="Credit card expiration date must be formatted MM/YY")
    var ccExpiration: String="",
    @get:Digits(integer=3, fraction=0, message="Invalid CVV")
    var ccCvv: String=""
) {

    fun toOrder(): Order {
        return Order(name, street, city, state, zip, ccNumber, ccExpiration, ccCvv)
    }

}
