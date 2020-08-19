package io.github.cafeteriaguild.lin.rt.exceptions

import io.github.cafeteriaguild.lin.rt.lib.nativelang.LinNativeError
import io.github.cafeteriaguild.lin.rt.lib.nativelang.routes.LinCatchable

class LinAssertionFailedException(message: String) : LinException(message), LinCatchable {
    override val thrown = LinNativeError("assertion_failed", message)
}
