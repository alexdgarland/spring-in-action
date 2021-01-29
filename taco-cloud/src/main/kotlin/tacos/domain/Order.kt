package tacos.domain

import java.util.Date

data class Order(
    var name: String,
    var street: String,
    var city: String,
    var state: String,
    var zip: String,
    var ccNumber: String,
    var ccExpiration: Date,
    var ccCvv: String
)

data class OrderViewModel(
    var name: String? = null,
    var street: String? = null,
    var city: String? = null,
    var state: String? = null,
    var zip: String? = null,
    var ccNumber: String? = null,
    var ccExpiration: String? = null,
    var ccCvv: String? = null
) {

    fun toOrder(): Order {
        return Order(
            validateNonNull(name, "name"),
            validateNonNull(street, "street"),
            validateNonNull(city, "city"),
            validateNonNull(state, "state"),
            validateNonNull(zip, "zip"),
            validateNonNull(ccNumber, "ccNumber"),
            validateAsDate(ccExpiration, "ccExpiration"),
            validateNonNull(ccCvv, "ccCvv")
        )
    }

}
