package tacos.data.impl.postgresql

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import tacos.data.IngredientRepository
import tacos.domain.Ingredient
import tacos.domain.IngredientType
import java.sql.ResultSet

class DataRetrievalException(message: String): Exception(message)

@Repository
class PostgresqlIngredientRepository(@Autowired val jdbc: JdbcTemplate): IngredientRepository {

    private val tableName = "ingredient"

    private fun mapRowToIngredient(rs: ResultSet, rowNum: Int): Ingredient {
        return Ingredient(
            rs.getString("ingredient_id"),
            rs.getString("ingredient_name"),
            IngredientType.valueOf(rs.getString("ingredient_type"))
        )
    }

    override fun findAll(): Iterable<Ingredient> {
        return jdbc.query(
            "SELECT ingredient_id, ingredient_name, ingredient_type FROM $tableName",
            this::mapRowToIngredient
        )
    }

    override fun findOne(id: String): Ingredient {
        return jdbc.queryForObject(
            "SELECT ingredient_id, ingredient_name, ingredient_type FROM $tableName WHERE ingredient_id=?",
            this::mapRowToIngredient,
            id
        )?: throw DataRetrievalException("Could not retrieve Ingredient with ID $id from database")
    }

    override fun save(ingredient: Ingredient): Ingredient {
        jdbc.update(
            "INSERT INTO $tableName (ingredient_id, ingredient_name, ingredient_type) VALUES (?, ?, ?)",
            ingredient.id?: -1,
            ingredient.name,
            ingredient.type.toString()
        )
        return ingredient
    }

}
