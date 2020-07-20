package io.github.cafeteriaguild.lin.rt.exc

open class LinNullException : LinException {
    constructor()

    constructor(message: String?) : super(message)

    constructor(message: String?, cause: Throwable?) : super(message, cause)

    constructor(cause: Throwable?) : super(cause)
}
