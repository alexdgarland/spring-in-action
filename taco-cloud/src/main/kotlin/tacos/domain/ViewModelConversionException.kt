package tacos.domain

import java.lang.Exception

class ViewModelConversionException: Exception {
    constructor(message: String): super(message)
    constructor(message: String, exception: Exception) : super("$message (captured error - \"${exception.message}\")")
}
