package tacos.data.impl.postgresql

import org.springframework.stereotype.Component
import java.util.Date

interface DateProvider {

    fun getCurrentDate(): Date

}

@Component
class RealDateProvider: DateProvider {

    override fun getCurrentDate(): Date {
        return Date()
    }

}
