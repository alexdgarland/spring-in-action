package tacos.domain

import java.lang.Exception
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date

val dateFormat = run {
    val format = SimpleDateFormat("yyyy-MM-dd")
    format.isLenient = false
    format
}

class ViewModelValidationException: Exception {
    constructor(message: String): super(message)
    constructor(message: String, exception: Exception) : super("$message (captured error - \"${exception.message}\")")
}

fun<T> validateNonNull(nullableField: T?, fieldName: String): T {
    return nullableField?: throw ViewModelValidationException("Value for field \"$fieldName\" was not supplied")
}

fun validateAsDate(nullableString: String?, fieldName: String): Date {
    val notNullString = validateNonNull(nullableString, fieldName)
    try {
        return dateFormat.parse(notNullString)
    }
    catch (pe: ParseException) {
        throw ViewModelValidationException(
            "String field $fieldName could not be parsed to a date using pattern \"${dateFormat.toPattern()}\"", pe
        )
    }
}
