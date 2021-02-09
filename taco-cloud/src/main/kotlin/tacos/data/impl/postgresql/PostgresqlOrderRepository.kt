package tacos.data.impl.postgresql

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import tacos.data.OrderRepository
import tacos.domain.Order


@Repository
class PostgresqlOrderRepository @Autowired constructor(val jdbc: JdbcTemplate): OrderRepository {

    override fun save(order: Order): Order {
        TODO("Not yet implemented")
    }

}
