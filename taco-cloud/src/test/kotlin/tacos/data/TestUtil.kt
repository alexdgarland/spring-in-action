package tacos.data

import org.junit.jupiter.api.Assertions
import java.util.*

fun assertDateSetRecently(date: Date?, description: String, dateFieldName: String) {
    val current = Date()
    Assertions.assertTrue(
        (current.time - date!!.time) < 1000,
        "$description - $dateFieldName $date does not match $current"
    )
}
