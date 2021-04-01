package tacos.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import javax.sql.DataSource

@Configuration
@EnableWebSecurity
class SecurityConfig(@Autowired val dataSource: DataSource): WebSecurityConfigurerAdapter() {

    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder(10)

    override fun configure(auth: AuthenticationManagerBuilder?) {
        auth
            ?.ldapAuthentication()
            ?.userSearchBase("ou=customers")
            ?.userSearchFilter("(uid={0})")
            ?.groupSearchBase("ou=groups")
            ?.groupSearchFilter("member={0}")
            ?.contextSource()
            ?.url("ldap://taco-ldap:389")
    }

}
