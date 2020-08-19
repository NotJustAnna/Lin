package io.github.cafeteriaguild.lin.rt.exceptions

import io.github.cafeteriaguild.lin.rt.lib.nativelang.LinNativeError
import io.github.cafeteriaguild.lin.rt.lib.nativelang.routes.LinCatchable

class LinIllegalAccessException(message: String) : LinException(message), LinCatchable {
    override val thrown = LinNativeError("illegal_access", message)
}
