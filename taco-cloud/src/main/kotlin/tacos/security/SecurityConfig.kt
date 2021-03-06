package tacos.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder


@Configuration
@EnableWebSecurity
class SecurityConfig(@Autowired val userDetailsService: UserDetailsService): WebSecurityConfigurerAdapter() {

    @Bean
    fun encoder(): PasswordEncoder? {
        return BCryptPasswordEncoder(10)
    }

    override fun configure(auth: AuthenticationManagerBuilder?) {
        auth
            ?.userDetailsService(userDetailsService)
            ?.passwordEncoder(encoder())
    }

    override fun configure(http: HttpSecurity?) {
        http
            ?.authorizeRequests()
            ?.antMatchers("/design", "/orders", "/orders/**")?.hasRole("USER")
            ?.antMatchers("/", "/**")?.permitAll()
            ?.and()?.formLogin()?.loginPage("/login")
    }
}
