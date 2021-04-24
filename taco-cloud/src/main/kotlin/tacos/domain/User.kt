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
    val id: Long? = null,
    private val username: String? = null,
    private val password: String? = null,
    val fullname: String? = null,
    val street: String? = null,
    val city: String? = null,
    val state: String? = null,
    val zip: String? = null,
    val phoneNumber: String? = null,
    val enabled: Boolean = true
) : UserDetails, AuditableEntity() {

    override fun getAuthorities(): Collection<GrantedAuthority?> {
        return Arrays.asList(SimpleGrantedAuthority("USER"))
    }

    override fun getPassword(): String {
        return password?: throw IllegalStateException("Password not set")
    }

    override fun getUsername(): String {
        return username?: throw IllegalStateException("User name not set")
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
