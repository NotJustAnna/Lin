package io.github.cafeteriaguild.lin.rt.exceptions

import io.github.cafeteriaguild.lin.rt.lib.LObj
import io.github.cafeteriaguild.lin.rt.lib.lang.LString
import io.github.cafeteriaguild.lin.rt.lib.nativelang.LinNativeError
import io.github.cafeteriaguild.lin.rt.lib.nativelang.routes.LinCatchable

class LinThrownException(override val thrown: LObj) : LinException(thrown.toString()), LinCatchable {
    constructor(type: LObj, message: LObj) : this(LinNativeError(type, message))
    constructor(type: String, message: String) : this(LString(type), LString(message))
    constructor(throwable: Throwable) : this("java/" + throwable.javaClass.simpleName, throwable.localizedMessage)
}
