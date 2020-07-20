package io.github.cafeteriaguild.lin.rt.lib

import io.github.cafeteriaguild.lin.ast.node.Declaration
import io.github.cafeteriaguild.lin.rt.LinInterpreter
import io.github.cafeteriaguild.lin.rt.lib.dsl.createGetter
import io.github.cafeteriaguild.lin.rt.scope.BasicScope
import io.github.cafeteriaguild.lin.rt.scope.Property
import io.github.cafeteriaguild.lin.rt.scope.Scope

class LObject(
    val name: String?,
    interpreter: LinInterpreter,
    param: Scope,
    body: List<Declaration>
) : LObj {
    private val scope = BasicScope(param)

    init {
        val getter = createGetter { this }
        scope.declareProperty("this", getter)
        for (declaration in body) {
            declaration.accept(interpreter, scope)
        }
    }

    override fun property(name: String): Property? {
        return scope.findProperty(name)
    }

    override fun toString(): String {
        (property("toString")?.get() as? LCallable)?.invoke(emptyList())
        return name ?: "object"
    }
}
