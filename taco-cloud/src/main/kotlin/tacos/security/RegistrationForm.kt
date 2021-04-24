package tacos.security

import org.springframework.security.crypto.password.PasswordEncoder
import tacos.domain.User

// TODO - validate fields from form (e.g. not null, max length) and escalate errors to page

data class RegistrationForm(
    val username: String? = null,
    val password: String? = null,
    val fullname: String? = null,
    val street: String? = null,
    val city: String? = null,
    val state: String? = null,
    val zip: String? = null,
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
