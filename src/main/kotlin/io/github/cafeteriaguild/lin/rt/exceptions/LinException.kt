package io.github.cafeteriaguild.lin.rt.exceptions

open class LinException : RuntimeException {
    constructor()

    constructor(message: String?) : super(message)

    constructor(message: String?, cause: Throwable?) : super(message, cause)

    constructor(cause: Throwable?) : super(cause)
}
