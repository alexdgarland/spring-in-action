package tacos.data

import org.springframework.data.repository.CrudRepository
import tacos.domain.TacoDesign

interface TacoDesignRepository: CrudRepository<TacoDesign, Long>
