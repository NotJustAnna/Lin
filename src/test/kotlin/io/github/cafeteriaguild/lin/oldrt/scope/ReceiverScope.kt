package io.github.cafeteriaguild.lin.oldrt.scope

import io.github.cafeteriaguild.lin.oldrt.types.functions.BoundFunction
import io.github.cafeteriaguild.lin.oldrt.types.functions.LFunction
import io.github.cafeteriaguild.lin.oldrt.types.properties.LProperty
import io.github.cafeteriaguild.lin.rt.lib.LObj
import io.github.cafeteriaguild.lin.rt.scope.Scope

class ReceiverScope(override val parent: Scope?, private val receiver: LObj) : AbstractScope() {
    override fun findProperty(name: String): LProperty {
        return receiver.type.memberProperties[name]?.bind(receiver)
            ?: super.findProperty(name)
    }

    override fun findLocalFunction(name: String): LFunction {
        return receiver.type.memberFunctions[name]?.let { BoundFunction(it, receiver) } ?: super.findLocalFunction(name)

    }
}