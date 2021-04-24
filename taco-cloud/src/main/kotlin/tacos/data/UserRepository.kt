package tacos.data

import org.springframework.data.repository.CrudRepository
import tacos.domain.User

interface UserRepository: CrudRepository<User, Long> {
    fun findByUsername(username: String): User?
}
