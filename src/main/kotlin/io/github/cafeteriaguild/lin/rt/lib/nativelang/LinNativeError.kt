package io.github.cafeteriaguild.lin.rt.lib.nativelang

import io.github.cafeteriaguild.lin.rt.lib.LObj
import io.github.cafeteriaguild.lin.rt.lib.lang.LString

class LinNativeError(val type: LObj, val message: LObj) : LinNativeObj() {
    constructor(type: String, message: String) : this(LString(type), LString(message))
    constructor(throwable: Throwable) : this("java/${throwable.javaClass.simpleName}", throwable.localizedMessage)

    init {
        setImmutableProperty("type", type)
        setImmutableProperty("message", message)
        declareToString { "$type: $message" }
    }
}