package tacos.data.impl.postgresql

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.PreparedStatementCreatorFactory
import org.springframework.jdbc.core.simple.SimpleJdbcInsert
import org.springframework.stereotype.Repository
import tacos.data.OrderRepository
import tacos.data.TacoDesignRepository
import tacos.data.impl.DataRetrievalException
import tacos.domain.Order
import java.sql.ResultSet
import java.sql.Timestamp
import java.sql.Types
import java.util.*

@Repository
class PostgresqlOrderRepository @Autowired constructor(
    val jdbc: JdbcTemplate,
    val dateProvider: DateProvider,
    val tacoDesignRepository: TacoDesignRepository
    ): OrderRepository
{

    private val orderInserter by lazy {
        SimpleJdbcInsert(jdbc).withTableName("taco_order")
        .usingGeneratedKeyColumns("taco_order_id")
    }

    private val orderTacoDesignInserter by lazy {
        SimpleJdbcInsert(jdbc).withTableName("taco_order_taco_designs")
    }

    private val updateOrderPscFactory = PreparedStatementCreatorFactory(
        "UPDATE taco_order " +
                "SET delivery_name=?, delivery_street=?, delivery_city=?, delivery_state=?, delivery_zip=?, " +
                "cc_number=?, cc_expiration=?, cc_cvv=?, placed_at=?, updated_at=? " +
                "WHERE taco_order_id=?",
        Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,  Types.VARCHAR,
        Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.TIMESTAMP, Types.TIMESTAMP,
        Types.BIGINT
    )

    override fun save(order: Order): Order {
        val saveDate = dateProvider.getCurrentDate()
        val saveTs = Timestamp(saveDate.getTime())

        val (id, placedDate) =
            if (order.id == null)
            {
                val values = mapOf(
                    "delivery_name" to order.name,
                    "delivery_street" to order.street,
                    "delivery_city" to order.city,
                    "delivery_state" to order.state,
                    "delivery_zip" to order.zip,
                    "cc_number" to order.ccNumber,
                    "cc_expiration" to order.ccExpiration,
                    "cc_cvv" to order.ccCvv,
                    "placed_at" to saveTs,
                    "updated_at" to saveTs
                )
                val orderId = orderInserter.executeAndReturnKey(values).toLong()
                Pair(orderId, saveDate)
            }
            else
            {
                val psc = updateOrderPscFactory.newPreparedStatementCreator(
                    listOf(order.name, order.street, order.city, order.state, order.zip,
                        order.ccNumber, order.ccExpiration, order.ccCvv, order.placedDate, saveTs,
                        order.id
                    )
                )
                jdbc.update(psc)
                jdbc.execute("DELETE FROM taco_order_taco_designs WHERE taco_order_id = ${order.id}")
                Pair(order.id, order.placedDate)
            }

        order.tacoDesigns.forEach { design ->
            orderTacoDesignInserter.execute(mapOf("taco_order_id" to id, "taco_design_id" to design.id))
        }

        return Order(id, order.name, order.street, order.city, order.state, order.zip,
            order.ccNumber, order.ccExpiration, order.ccCvv, order.tacoDesigns, placedDate, saveDate
        )
    }

    private fun mapRowToOrder(rs: ResultSet, rowNum: Int): Order {
        return Order(
            rs.getLong("taco_order_id"),
            rs.getString("delivery_name"),
            rs.getString("delivery_street"),
            rs.getString("delivery_city"),
            rs.getString("delivery_state"),
            rs.getString("delivery_zip"),
            rs.getString("cc_number"),
            rs.getString("cc_expiration"),
            rs.getString("cc_cvv"),
            mutableListOf(),
            Date(rs.getTimestamp("placed_at").time),
            Date(rs.getTimestamp("updated_at").time)
        )
    }

    override fun findOne(orderId: Long): Order {
        val order = jdbc.queryForObject(
            "SELECT taco_order_id, delivery_name, delivery_street, delivery_city, delivery_state, delivery_zip, " +
                    "cc_number, cc_expiration, cc_cvv, placed_at, updated_at " +
                    "FROM taco_order WHERE taco_order_id=?",
            this::mapRowToOrder,
            orderId
        )?: throw DataRetrievalException("Could not retrieve Order with ID $orderId from database")
        // This is a bit hacky (creates other repo instance internally and explicitly uses N+1 selects)
        // but is only a temp thing before we convert to using entities and at least avoids substantial code duplication
//        val tacoDesignRepository = TacoDesignRepository(jdbc, dateProvider)
        jdbc.query(
            "SELECT taco_design_id FROM taco_order_taco_designs WHERE taco_order_id=$orderId"
        ){ rs ->
            order.tacoDesigns.add(tacoDesignRepository.findById(rs.getLong("taco_design_id")).get())
        }
        // </hack>
        return order
    }

}
