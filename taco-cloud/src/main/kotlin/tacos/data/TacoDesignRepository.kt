package tacos.data

import tacos.domain.TacoDesign

interface TacoDesignRepository {

    fun save(design: TacoDesign): TacoDesign

}
