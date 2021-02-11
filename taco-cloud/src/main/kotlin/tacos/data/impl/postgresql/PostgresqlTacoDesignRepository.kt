package tacos.data.impl.postgresql

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.PreparedStatementCreatorFactory
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository
import tacos.data.TacoDesignRepository
import tacos.data.impl.DataRetrievalException
import tacos.domain.TacoDesign
import java.sql.ResultSet
import java.sql.Timestamp
import java.sql.Types
import java.util.*


@Repository
class PostgresqlTacoDesignRepository @Autowired constructor(val jdbc: JdbcTemplate, val dateProvider: DateProvider):
    TacoDesignRepository
{

    private val createDesignPscFactory = run {
        val factory = PreparedStatementCreatorFactory(
            "INSERT INTO taco_design (taco_design_name, created_at, updated_at) VALUES (?, ?, ?)",
            Types.VARCHAR, Types.TIMESTAMP, Types.TIMESTAMP
        )
        factory.setReturnGeneratedKeys(true)
        factory
    }

    private val updateDesignPscFactory = PreparedStatementCreatorFactory(
        "UPDATE taco_design SET taco_design_name=?, created_at=?, updated_at=? WHERE taco_design_id=?",
        Types.VARCHAR, Types.TIMESTAMP, Types.TIMESTAMP, Types.BIGINT
    )

    //  Not sure if this offers a better option for audit dates once we switch to entities - http://bit.ly/2YZNkOq

    override fun save(design: TacoDesign): TacoDesign {
        val saveDate = dateProvider.getCurrentDate()
        val saveTs = Timestamp(saveDate.getTime())

        val (id, createdDate) =
            if (design.id == null)
            {
                val psc = createDesignPscFactory.newPreparedStatementCreator(listOf(design.name, saveTs, saveTs))
                val keyHolder = GeneratedKeyHolder()
                jdbc.update(psc, keyHolder)
                Pair(keyHolder.keys?.get("taco_design_id") as Long, saveDate)
            }
            else
            {
                val psc = updateDesignPscFactory.newPreparedStatementCreator(
                    listOf(design.name, design.createdDate, saveTs, design.id)
                )
                jdbc.update(psc)
                jdbc.execute("DELETE FROM taco_design_ingredients WHERE taco_design_id = ${design.id}")
                Pair(design.id, design.createdDate)
            }

        design.ingredients.forEach {
            jdbc.update("INSERT INTO taco_design_ingredients (taco_design_id, ingredient_id) VALUES (?, ?)", id, it)
        }

        return TacoDesign(id, design.name, design.ingredients, createdDate, updatedDate = saveDate)
    }

    private fun mapRowToTacoDesign(rs: ResultSet, rowNum: Int): TacoDesign {
        return TacoDesign(
            rs.getLong("taco_design_id"),
            rs.getString("taco_design_name"),
            emptyList(),
            Date(rs.getTimestamp("created_at").time),
            Date(rs.getTimestamp("updated_at").time)
        )
    }

    override fun findOne(designId: Long): TacoDesign {
        val tacoDesign = jdbc.queryForObject(
            "SELECT taco_design_id, taco_design_name, created_at, updated_at FROM taco_design WHERE taco_design_id=?",
            this::mapRowToTacoDesign,
            designId
        )?: throw DataRetrievalException("Could not retrieve TacoDesign with ID $designId from database")
        val ingredients = mutableListOf<String>()
        jdbc.query(
            "SELECT ingredient_id FROM taco_design_ingredients WHERE taco_design_id=$designId"
        ) { rs ->
            ingredients.add(rs.getString("ingredient_id"))
        }
        tacoDesign.ingredients = ingredients
        return tacoDesign
    }

}
