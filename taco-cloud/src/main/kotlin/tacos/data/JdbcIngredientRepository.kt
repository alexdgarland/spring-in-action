package tacos.data

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import tacos.domain.Ingredient
import tacos.domain.IngredientType
import java.sql.ResultSet

class DataRetrievalException(message: String): Exception(message)

@Repository
class JdbcIngredientRepository(@Autowired val jdbc: JdbcTemplate): IngredientRepository {

    private val tableName = "Ingredient"

    private fun mapRowToIngredient(rs: ResultSet, rowNum: Int): Ingredient {
        return Ingredient(
            rs.getString("id"),
            rs.getString("name"),
            IngredientType.valueOf(rs.getString("type"))
        )
    }

    override fun findAll(): Iterable<Ingredient> {
        return jdbc.query("SELECT id, name, type FROM $tableName", this::mapRowToIngredient)
    }

    override fun findOne(id: String): Ingredient {
        return jdbc.queryForObject(
            "SELECT id, name, type FROM $tableName WHERE id=?",
            this::mapRowToIngredient,
            id
        )?: throw DataRetrievalException("Could not retrieve Ingredient with ID $id from database")
    }

    override fun save(ingredient: Ingredient): Ingredient {
        jdbc.update(
            "INSERT INTO $tableName (id, name, type) VALUES (?, ?, ?)",
            ingredient.id?: -1,
            ingredient.name,
            ingredient.type.toString()
        )
        return ingredient
    }

}
