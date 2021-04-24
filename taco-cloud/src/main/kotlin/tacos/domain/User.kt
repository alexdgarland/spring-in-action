package tacos.domain

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.*
import javax.persistence.*

// TODO - validate fields from form (e.g. not null, max length) and escalate errors to page
@Entity(name = "users")
data class User(
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    private var username: String? = null,
    private var password: String? = null,
    var fullname: String? = null,
    var street: String? = null,
    var city: String? = null,
    var state: String? = null,
    var zip: String? = null,
    var phoneNumber: String? = null,
    var enabled: Boolean = true
) : UserDetails, AuditableEntity() {

    override fun getAuthorities(): Collection<GrantedAuthority?> {
        return Arrays.asList(SimpleGrantedAuthority("USER"))
    }

    override fun getPassword(): String {
        return password?: throw IllegalStateException("Password not set")
    }

    fun setPassword(newPassword: String) {
        password = newPassword
    }

    override fun getUsername(): String {
        return username?: throw IllegalStateException("User name not set")
    }

    fun setUserName(newUsername: String) {
        username = newUsername
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return enabled
    }

    companion object {
        private const val serialVersionUID = 1L
    }
}
