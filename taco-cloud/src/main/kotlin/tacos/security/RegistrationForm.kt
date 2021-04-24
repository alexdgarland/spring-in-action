package tacos.security

import org.springframework.security.crypto.password.PasswordEncoder
import tacos.domain.User

data class RegistrationForm(
    private val username: String? = null,
    private val password: String? = null,
    private val fullname: String? = null,
    private val street: String? = null,
    private val city: String? = null,
    private val state: String? = null,
    private val zip: String? = null,
    private val phone: String? = null
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
