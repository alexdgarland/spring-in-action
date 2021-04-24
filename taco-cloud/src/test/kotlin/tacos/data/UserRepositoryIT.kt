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

    @Test
    fun canSaveNewUser() {
        val username = "bbobertson"
        val password = "##mmpie"
        val fullname = "Bob Bobertson"
        val street = "Drury Lane"
        val city = "Pietown"
        val state = "WB"
        val zip = "111222"
        val phoneNumber = "555-222-777"

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
        val originalUsername = "wbull"
        val originalPassword = "##melikepie"
        val originalFullname = "Weebl"
        val originalStreet = "Cobblers Hill"
        val originalCity = "Pietown"
        val originalState = "WB"
        val originalZip = "111222"
        val originalPhoneNumber = "555-333-444"

        val user = User(
            null,
            originalUsername,
            originalPassword,
            originalFullname,
            originalStreet,
            originalCity,
            originalState,
            originalZip,
            originalPhoneNumber
        )
        val originalSavedUser = repository.save(user)
        val userId = originalSavedUser.id?: fail("Test should get a returned ID")
        val originalCreatedDate = originalSavedUser.createdDate
        val originalUpdatedDate = originalSavedUser.updatedDate

        // Sleep for one second so can be sure that last modified date will change
        Thread.sleep(1000)

        // Run an update
        val newFullname = "Weebl Bull"
        val newPassword = "##yumyumpie"
        user.fullname = newFullname
        user.password = newPassword
        val updatedSavedUser = repository.save(user)

        // Assert
        fun assertExpectedUpdatedUser(actualUser: User, description: String) {
            assertEquals(newFullname, actualUser.fullname, "$description - Updated full name not as expected")
            assertEquals(newPassword, actualUser.password, "$description - Updated password not as expected")
            assertEquals(originalCreatedDate, actualUser.createdDate, "$description - Created date not as expected")
            assertDateSetRecently(actualUser.updatedDate, description, "Updated date")
            assertNotEquals(originalUpdatedDate, actualUser.updatedDate, "Updated date not changed")
        }
        assertExpectedUpdatedUser(updatedSavedUser, "Saved user")
        assertExpectedUpdatedUser(repository.findById(userId).get(), "Retrieved user")
    }

}

