package io.github.cafeteriaguild.lin.rt.scope

import io.github.cafeteriaguild.lin.rt.types.LTAny
import io.github.cafeteriaguild.lin.rt.types.LType
import io.github.cafeteriaguild.lin.rt.types.functions.LFunction
import io.github.cafeteriaguild.lin.rt.types.properties.LObjectProperty
import io.github.cafeteriaguild.lin.rt.types.properties.LProperty
import io.github.cafeteriaguild.lin.rt.types.properties.LocalProperty
import java.util.concurrent.ConcurrentHashMap

class FunctionScope(override val parent: Scope? = null) : AbstractScope() {

    val localFunctions = ConcurrentHashMap<String, LFunction>()
    private val localProperties = ConcurrentHashMap<String, LProperty>()
    val extensionFunctions = ConcurrentHashMap<Pair<LType, String>, LFunction>()
    val extensionProperties = ConcurrentHashMap<Pair<LType, String>, LObjectProperty>()

    override fun findProperty(name: String): LProperty {
        return localProperties[name] ?: super.findProperty(name)
    }

    override fun declareLocalProperty(name: String, mutable: Boolean): LProperty {
        val p = LocalProperty(LTAny, name, mutable)
        localProperties[name] = p
        return p
    }

    override fun findLocalFunction(name: String): LFunction {
        return localFunctions[name] ?: super.findLocalFunction(name)
    }

    override fun findExtensionProperty(type: LType, name: String): LObjectProperty {
        return extensionProperties[type to name] ?: super.findExtensionProperty(type, name)
    }

    override fun findExtensionFunctions(type: LType, name: String): LFunction {
        return extensionFunctions[type to name] ?: super.findExtensionFunctions(type, name)
    }
}