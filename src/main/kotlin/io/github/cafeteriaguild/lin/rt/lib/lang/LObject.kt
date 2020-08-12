package io.github.cafeteriaguild.lin.rt.lib.lang

import io.github.cafeteriaguild.lin.ast.node.Declaration
import io.github.cafeteriaguild.lin.rt.LinInterpreter
import io.github.cafeteriaguild.lin.rt.lib.nativelang.AbstractObj
import io.github.cafeteriaguild.lin.rt.lib.nativelang.properties.LazyProperty
import io.github.cafeteriaguild.lin.rt.lib.nativelang.properties.Property
import io.github.cafeteriaguild.lin.rt.scope.BasicScope
import io.github.cafeteriaguild.lin.rt.scope.Scope

class LObject(
    val name: String?,
    interpreter: LinInterpreter,
    param: Scope,
    body: List<Declaration>
) : AbstractObj() {
    private val scope = BasicScope(param)

    init {
        scope.declareProperty("this", LazyProperty { this })
        for (declaration in body) {
            declaration.accept(interpreter, scope)
        }
    }

    override fun properties(): Set<String> {
        return scope.properties.keys
    }

    override fun propertyOf(name: String): Property? {
        return scope.findProperty(name)
    }
}
