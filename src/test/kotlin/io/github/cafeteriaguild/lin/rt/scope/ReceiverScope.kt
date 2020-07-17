package io.github.cafeteriaguild.lin.rt.scope

import io.github.cafeteriaguild.lin.rt.lib.LObj
import io.github.cafeteriaguild.lin.rt.types.functions.BoundFunction
import io.github.cafeteriaguild.lin.rt.types.functions.LFunction
import io.github.cafeteriaguild.lin.rt.types.properties.LProperty

class ReceiverScope(override val parent: Scope?, private val receiver: LObj) : AbstractScope() {
    override fun findProperty(name: String): LProperty {
        return receiver.type.memberProperties[name]?.bind(receiver)
            ?: super.findProperty(name)
    }

    override fun findLocalFunction(name: String): LFunction {
        return receiver.type.memberFunctions[name]?.let { BoundFunction(it, receiver) } ?: super.findLocalFunction(name)

    }
}