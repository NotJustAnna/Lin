package io.github.cafeteriaguild.lin.oldrt.scope

import io.github.cafeteriaguild.lin.oldrt.types.LType
import io.github.cafeteriaguild.lin.oldrt.types.functions.LFunction
import io.github.cafeteriaguild.lin.oldrt.types.properties.LObjectProperty
import io.github.cafeteriaguild.lin.oldrt.types.properties.LProperty
import io.github.cafeteriaguild.lin.rt.scope.Scope

abstract class AbstractScope : Scope {
    protected abstract val parent: Scope?

    override fun findProperty(name: String): LProperty {
        return parent?.findProperty(name) ?: throw RuntimeException("Property doesn't exist.")
    }

    override fun declareLocalProperty(name: String, mutable: Boolean): LProperty {
        return parent?.declareLocalProperty(name, mutable) ?: throw RuntimeException("Can't declare properties.")
    }

    override fun findLocalFunction(name: String): LFunction {
        return parent?.findLocalFunction(name) ?: throw RuntimeException("Function doesn't exist.")
    }

    override fun findExtensionProperty(type: LType, name: String): LObjectProperty {
        return parent?.findExtensionProperty(type, name) ?: throw RuntimeException("Extension Property doesn't exist.")
    }

    override fun findExtensionFunctions(type: LType, name: String): LFunction {
        return parent?.findExtensionFunctions(type, name) ?: throw RuntimeException("Extension function doesn't exist.")
    }
}