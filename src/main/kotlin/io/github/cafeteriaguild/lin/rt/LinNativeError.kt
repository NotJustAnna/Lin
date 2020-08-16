package io.github.cafeteriaguild.lin.rt

import io.github.cafeteriaguild.lin.rt.lib.LObj
import io.github.cafeteriaguild.lin.rt.lib.lang.LString
import io.github.cafeteriaguild.lin.rt.lib.nativelang.LinNativeObj

class LinNativeError(val type: LObj, val message: LObj) : LinNativeObj() {
    constructor(type: String, message: String) : this(LString(type), LString(message))
    constructor(throwable: Throwable) : this(throwable.javaClass.simpleName, throwable.localizedMessage)

    init {
        setImmutableProperty("type", type)
        setImmutableProperty("message", message)
        declareToString { "$type: $message" }
    }
}