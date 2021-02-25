package tacos.data.impl.postgresql

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.PreparedStatementCreatorFactory
import org.springframework.jdbc.core.simple.SimpleJdbcInsert
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

    private val tacoDesignInserter by lazy {
        SimpleJdbcInsert(jdbc)
        .withTableName("taco_design")
        .usingGeneratedKeyColumns("taco_design_id")
    }

    private val tacoDesignIngredientInserter by lazy {
        SimpleJdbcInsert(jdbc)
        .withTableName("taco_design_ingredients")
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
                val designId = tacoDesignInserter.executeAndReturnKey(
                    mapOf("taco_design_name" to design.name, "created_at" to saveTs, "updated_at" to saveTs)
                ).toLong()
                Pair(designId, saveDate)
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

        design.ingredients.forEach { ingredientId ->
            tacoDesignIngredientInserter.execute(mapOf("taco_design_id" to id, "ingredient_id" to ingredientId))
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
