package tacos.data.impl.postgresql

import java.util.*

class FixedTestDateProvider(val date: Date): DateProvider {

    override fun getCurrentDate(): Date {
        return date
    }

}
