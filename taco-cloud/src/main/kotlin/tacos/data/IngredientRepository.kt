package tacos.data

import org.springframework.data.repository.CrudRepository
import tacos.domain.Ingredient

interface IngredientRepository: CrudRepository<Ingredient, String>
