package io.github.cafeteriaguild.lin.rt.scope

import io.github.cafeteriaguild.lin.rt.types.LType
import io.github.cafeteriaguild.lin.rt.types.functions.LFunction
import io.github.cafeteriaguild.lin.rt.types.properties.LObjectProperty
import io.github.cafeteriaguild.lin.rt.types.properties.LProperty

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