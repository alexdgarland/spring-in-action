package tacos.data

import tacos.domain.Order

interface OrderRepository {

    fun save(order: Order): Order

}
