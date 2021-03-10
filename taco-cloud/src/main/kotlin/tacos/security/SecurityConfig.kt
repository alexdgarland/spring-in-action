package tacos.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@Configuration
@EnableWebSecurity
class SecurityConfig: WebSecurityConfigurerAdapter() {

    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder(10)

    override fun configure(auth: AuthenticationManagerBuilder?) {
        val inMemAuth = auth?.inMemoryAuthentication()
        val encoder = passwordEncoder()

        fun addUser(name: String, password: String) = inMemAuth
            ?.withUser(name)
            ?.password(encoder.encode(password))
            ?.authorities("ROLE_USER")

        addUser("buzz", "infinity")
        addUser("woody", "bullseye")
    }

}
