package tacos.domain

import org.hibernate.validator.constraints.CreditCardNumber
import org.springframework.stereotype.Component
import javax.persistence.*
import javax.validation.constraints.Digits
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

@Component
@Entity(name = "taco_order")
data class Order(
    @Id
    @Column(name = "taco_order_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "delivery_name")
    @get:NotBlank(message="Name is required")
    @get:Size(max=50, message="Name cannot be more than 50 characters")
    var name: String="",

    @Column(name = "delivery_street")
    @get:NotBlank(message="Street is required")
    @get:Size(max=50, message="Street cannot be more than 50 characters")
    var street: String="",

    @Column(name = "delivery_city")
    @get:NotBlank(message="City is required")
    @get:Size(max=50, message="City cannot be more than 50 characters")
    var city: String="",

    @Column(name = "delivery_state")
    @get:Size(min=2, max=2, message="State must be exactly two characters")
    var state: String="",

    @Column(name = "delivery_zip")
    @get:NotBlank(message="Zip code is required")
    @get:Size(max=10, message="Zip code cannot be more than 10 characters")
    var zip: String="",

    @Column(name = "cc_number")
    @get:CreditCardNumber(message="Not a valid credit card number")
    var ccNumber: String="",

    @Column(name = "cc_expiration")
    @get:Pattern(regexp="^(0[1-9]|1[0-2])([\\/])([1-9][0-9])$",
        message="Credit card expiration date must be formatted MM/YY")
    var ccExpiration: String="",

    @Column(name = "cc_cvv")
    @get:Digits(integer=3, fraction=0, message="Invalid CVV")
    var ccCvv: String="",

    @ManyToMany(targetEntity = TacoDesign::class)
    @JoinTable(
        name = "taco_order_taco_designs",
        joinColumns = [JoinColumn(name ="taco_order_id")],
        inverseJoinColumns = [JoinColumn(name = "taco_design_id")]
    )
    val tacoDesigns: MutableList<TacoDesign> = mutableListOf()
): AuditableEntity()
