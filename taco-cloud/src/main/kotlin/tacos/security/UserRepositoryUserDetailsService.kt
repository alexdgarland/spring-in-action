package tacos.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import tacos.data.UserRepository

@Service
class UserRepositoryUserDetailsService @Autowired constructor(private val userRepo: UserRepository): UserDetailsService
{
    override fun loadUserByUsername(username: String) = userRepo
        .findByUsername(username)
        ?: throw UsernameNotFoundException("User '$username' not found")
}
