package tacos.web

import org.slf4j.LoggerFactory
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import tacos.data.UserRepository
import tacos.security.RegistrationForm

@Controller
@RequestMapping("/register")
class RegistrationController(
    private val userRepo: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    @GetMapping
    fun registerForm(): String {
        logger.info("Loading registration form page")
        return "registration"
    }

    @PostMapping
    fun processRegistration(form: RegistrationForm): String {
        userRepo.save(form.toUser(passwordEncoder))
        return "redirect:/login"
    }
}
