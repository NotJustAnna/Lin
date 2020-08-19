package io.github.cafeteriaguild.lin.rt.exceptions

import io.github.cafeteriaguild.lin.rt.lib.nativelang.LinNativeError
import io.github.cafeteriaguild.lin.rt.lib.nativelang.routes.LinCatchable

class LinUnboundSignalException(message: String, cause: Throwable) : LinException(message, cause), LinCatchable {
    override val thrown = LinNativeError("unbound_signal", message)
}
