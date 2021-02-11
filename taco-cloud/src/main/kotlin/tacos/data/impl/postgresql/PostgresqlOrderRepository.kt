package tacos.data.impl.postgresql

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.PreparedStatementCreatorFactory
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository
import tacos.data.OrderRepository
import tacos.domain.Order
import java.sql.Timestamp
import java.sql.Types

@Repository
class PostgresqlOrderRepository @Autowired constructor(val jdbc: JdbcTemplate, val dateProvider: DateProvider):
    OrderRepository
{

    private val createOrderPscFactory = run {
        val factory = PreparedStatementCreatorFactory(
            "INSERT INTO taco_order " +
                    "(delivery_name, delivery_street, delivery_city, delivery_state, delivery_zip, " +
                    "cc_number, cc_expiration, cc_cvv, placed_at, updated_at) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
            Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,  Types.VARCHAR,
            Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.TIMESTAMP, Types.TIMESTAMP
        )
        factory.setReturnGeneratedKeys(true)
        factory
    }

    override fun save(order: Order): Order {
        // TODO - handle updates to existing orders as well as creating new
        // TODO - once this is done it may make sense to refactor to share code with PostgresqlTacoDesignRepository
        // (as an exercise before moving everything to use entities)

        val saveDate = dateProvider.getCurrentDate()
        val saveTs = Timestamp(saveDate.getTime())

        val psc = createOrderPscFactory.newPreparedStatementCreator(
            listOf(order.name, order.street, order.city, order.state, order.zip,
                order.ccNumber, order.ccExpiration, order.ccCvv, saveTs, saveTs
            )
        )
        val keyHolder = GeneratedKeyHolder()
        jdbc.update(psc, keyHolder)
        val generatedOrderId = keyHolder.keys?.get("taco_order_id") as Long

        order.tacoDesigns.forEach { design ->
            jdbc.update("INSERT INTO taco_order_taco_designs (taco_order_id, taco_design_id) VALUES (?, ?)",
                generatedOrderId, design.id)
        }

        return Order(generatedOrderId, order.name, order.street, order.city, order.state, order.zip,
            order.ccNumber, order.ccExpiration, order.ccCvv, order.tacoDesigns, saveDate, saveDate
        )
    }

    // TODO - implement findOne() (add to interface first)

}
