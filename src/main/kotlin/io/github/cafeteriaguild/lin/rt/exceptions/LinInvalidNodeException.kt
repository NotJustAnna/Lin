package io.github.cafeteriaguild.lin.rt.exceptions

import io.github.cafeteriaguild.lin.rt.lib.nativelang.LinNativeError
import io.github.cafeteriaguild.lin.rt.lib.nativelang.routes.LinCatchable

class LinInvalidNodeException(message: String) : LinException(message), LinCatchable {
    override val thrown = LinNativeError("invalid_node", message)
}
