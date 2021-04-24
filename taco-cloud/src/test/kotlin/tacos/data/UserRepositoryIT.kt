package tacos.data

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.test.context.ContextConfiguration
import org.testcontainers.junit.jupiter.Testcontainers
import tacos.domain.User

@Testcontainers
@SpringBootTest(properties = ["spring.main.allow-bean-definition-overriding=true"])
@ContextConfiguration(initializers = [PostgresContainerTestInitializer::class])
@EnableJpaAuditing
class UserRepositoryIT(@Autowired val repository: UserRepository) {

    private val username = "bbobertson"
    private val password = "##mmpie"
    private val fullname = "Bob Bobertson"
    private val street = "Drury Lane"
    private val city = "Pietown"
    private val state = "WB"
    private val zip = "111222"
    private val phoneNumber = "555-222-777"

    @Test
    fun canSaveNewUser() {
        val originalUser = User(null, username, password, fullname, street, city, state, zip, phoneNumber)
        val savedUser = repository.save(originalUser)

        fun assertExpectedSavedUser(actualUser: User, description: String) {
            assertNotNull(actualUser.id, "User ID is null")
            assertEquals(username, actualUser.username, "$description - Username not as expected")
            assertEquals(password, actualUser.password, "$description - Password ${actualUser.password} not as expected $password")
            assertEquals(fullname, actualUser.fullname, "$description - Fullname not as expected")
            assertEquals(street, actualUser.street, "$description - Street not as expected")
            assertEquals(city, actualUser.city, "$description - City not as expected")
            assertEquals(state, actualUser.state, "$description - State not as expected")
            assertEquals(zip, actualUser.zip, "$description - Zip not as expected")
            assertEquals(phoneNumber, actualUser.phoneNumber, "$description - Phone number not as expected")
            assertTrue(actualUser.enabled, "$description - Enabled flag not defaulted to true")
            assertDateSetRecently(actualUser.createdDate, description, "Created date")
            assertDateSetRecently(actualUser.updatedDate, description, "Updated date")
        }

        assertExpectedSavedUser(savedUser, "Saved user")
        assertExpectedSavedUser(repository.findById(savedUser.id!!).get(), "User retrieved by ID")
        assertExpectedSavedUser(
            repository.findByUsername(username)?: fail("Repository found no user by expected username"),
            "User retrieved by username"
        )
    }

    @Test
    fun canUpdateExistingUser() {
        // TODO - flesh out this test using an altered version of commented code below (from TacoDesign IT)
//        // Set up preexisting record
//        val tacoDesign = TacoDesign(name=designName, ingredients=ingredients)
//        val originalSavedDesign = repository.save(tacoDesign)
//        val designId = originalSavedDesign.id?: fail("Test should get a returned ID")
//        val originalCreatedDate = originalSavedDesign.createdDate
//        val originalUpdatedDate = originalSavedDesign.updatedDate
//
//        // Sleep for one second so can be sure that last modified date will change
//        Thread.sleep(1000)
//
//        // Run an update
//        val newName = "New and improved!"
//        val newIngredients = getIngredients("FLTO", "GRBF", "TMTO", "JACK", "SLSA")
//        tacoDesign.name = newName
//        tacoDesign.ingredients = newIngredients
//        val updatedSavedTacoDesign = repository.save(tacoDesign)
//
//        // Assert
//        fun assertExpectedUpdatedDesign(actualDesign: TacoDesign, description: String) {
//            assertEquals(newName, actualDesign.name, "$description - Name not as expected")
//            assertEquals(newIngredients, actualDesign.ingredients, "$description - Ingredients not as expected")
//            assertEquals(originalCreatedDate, actualDesign.createdDate, "$description - Created date not as expected")
//            assertDateSetRecently(actualDesign.updatedDate, description, "Updated date")
//            assertNotEquals(originalUpdatedDate, actualDesign.updatedDate, "Updated date not changed")
//        }
//        assertExpectedUpdatedDesign(updatedSavedTacoDesign, "Saved design")
//        assertExpectedUpdatedDesign(repository.findById(designId).get(), "Retrieved design")
    }

}

