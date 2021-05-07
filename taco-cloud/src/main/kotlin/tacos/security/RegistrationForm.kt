package tacos.security

import org.springframework.security.crypto.password.PasswordEncoder
import tacos.domain.User
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

data class RegistrationForm(
    @get:NotBlank(message="Username is required")
    @get:Size(max=50, message="Username cannot be more than 50 characters")
    val username: String? = null,

    @get:NotBlank(message="Password is required")
    // No point in validating length here as what gets stored in the database is output of encoder
    val password: String? = null,

    @get:NotBlank(message="Fullname is required")
    @get:Size(max=100, message="Fullname cannot be more than 100 characters")
    val fullname: String? = null,

    @get:NotBlank(message="Street is required")
    @get:Size(max=100, message="Street cannot be more than 100 characters")
    val street: String? = null,

    @get:NotBlank(message="City is required")
    @get:Size(max=100, message="City cannot be more than 100 characters")
    val city: String? = null,

    @get:NotBlank(message="State is required")
    @get:Size(min=2, max=2, message="State must be exactly two characters")
    val state: String? = null,

    @get:NotBlank(message="Zip code is required")
    @get:Size(max=10, message="Zip code cannot be more than 10 characters")
    val zip: String? = null,

    @get:NotBlank(message="Phone number is required")
    @get:Size(max=20, message="Phone number cannot be more than 20 characters")
    // TODO - Could potentially do more meaningful validation of phone number format here
    val phone: String? = null
) {
    fun toUser(passwordEncoder: PasswordEncoder) = User(
        null,
        username,
        passwordEncoder.encode(password),
        fullname,
        street,
        city,
        state,
        zip,
        phone
    )
}
