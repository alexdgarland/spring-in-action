package tacos.data.impl.postgresql

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import tacos.data.IngredientRepository
import tacos.data.impl.DataRetrievalException
import tacos.domain.Ingredient
import tacos.domain.IngredientType
import java.sql.ResultSet

@Repository
class PostgresqlIngredientRepository @Autowired constructor(val jdbc: JdbcTemplate): IngredientRepository {

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

    override fun findAllAsMap(): Map<String, Ingredient> {
        return findAll().map { it.id to it}.toMap()
    }

    override fun findOne(ingredientId: String): Ingredient {
        return jdbc.queryForObject(
            "SELECT ingredient_id, ingredient_name, ingredient_type FROM $tableName WHERE ingredient_id=?",
            this::mapRowToIngredient,
            ingredientId
        )?: throw DataRetrievalException("Could not retrieve Ingredient with ID $ingredientId from database")
    }

    override fun save(ingredient: Ingredient): Ingredient {
        jdbc.update(
            "INSERT INTO $tableName (ingredient_id, ingredient_name, ingredient_type)\n" +
                    "    VALUES (?, ?, ?)" +
                    "    ON CONFLICT(ingredient_id) DO\n" +
                    "        UPDATE SET\n" +
                    "            ingredient_name = EXCLUDED.ingredient_name,\n" +
                    "            ingredient_type = EXCLUDED.ingredient_type",
            ingredient.id,
            ingredient.name,
            ingredient.type.toString()
        )
        return ingredient
    }

}
