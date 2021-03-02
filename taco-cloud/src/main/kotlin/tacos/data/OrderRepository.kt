package tacos.data

import org.springframework.data.repository.CrudRepository
import tacos.domain.Order

interface OrderRepository: CrudRepository<Order, Long>
