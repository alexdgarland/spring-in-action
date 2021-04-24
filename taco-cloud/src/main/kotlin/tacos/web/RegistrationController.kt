package tacos.web

import org.slf4j.LoggerFactory
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import tacos.data.UserRepository
import tacos.security.RegistrationForm
import javax.validation.Valid

@Controller
@RequestMapping("/register")
class RegistrationController(
    private val userRepo: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    @GetMapping
    fun registerForm(model: Model): String {
        logger.info("Loading registration form page")
        model.addAttribute("registrationForm", RegistrationForm())
        return "registrationForm"
    }

    @PostMapping
    fun processRegistration(@Valid @ModelAttribute(name = "registrationForm") form: RegistrationForm): String {
        // In a real app we would be careful NOT to log out the password at this stage! But here it doesn't matter.
        logger.info("Got data from form: $form")
        userRepo.save(form.toUser(passwordEncoder))
        return "redirect:/login"
    }
}
