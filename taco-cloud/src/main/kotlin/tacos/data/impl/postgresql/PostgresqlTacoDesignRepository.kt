package tacos.data.impl.postgresql

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.PreparedStatementCreatorFactory
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository
import tacos.data.TacoDesignRepository
import tacos.domain.TacoDesign
import java.sql.Timestamp
import java.sql.Types
import java.util.*


@Repository
class PostgresqlTacoDesignRepository @Autowired constructor(val jdbc: JdbcTemplate, val dateProvider: DateProvider):
    TacoDesignRepository
{

    private val designInsertPscFactory = run {
        val factory = PreparedStatementCreatorFactory(
            "INSERT INTO taco_design (taco_design_name, created_at) VALUES (?, ?)",
            Types.VARCHAR, Types.TIMESTAMP
        )
        factory.setReturnGeneratedKeys(true)
        factory
    }

    private fun saveDesignInfo(designName: String, createdDate: Date): Long {
        val psc = designInsertPscFactory.newPreparedStatementCreator(
            listOf(designName, Timestamp(createdDate.getTime()))
        )
        val keyHolder = GeneratedKeyHolder()
        jdbc.update(psc, keyHolder)
        return keyHolder.keys?.get("taco_design_id") as Long
    }

    private fun saveIngredientToDesign(ingredientId: String, tacoDesignId: Long) {
        jdbc.update(
            "INSERT INTO taco_design_ingredients (taco_design_id, ingredient_id) VALUES (?, ?)",
            tacoDesignId, ingredientId
        )
    }

    override fun save(design: TacoDesign): TacoDesign {
        val createdDate = dateProvider.getCurrentDate()
        val designId = saveDesignInfo(design.name, createdDate)
        design.ingredients.forEach { saveIngredientToDesign(it, designId) }
        return TacoDesign(id=designId, name=design.name, ingredients = design.ingredients, createdDate)
    }

}
