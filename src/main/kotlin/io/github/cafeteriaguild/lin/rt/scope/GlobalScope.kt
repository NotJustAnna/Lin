package io.github.cafeteriaguild.lin.rt.scope

import io.github.cafeteriaguild.lin.rt.exc.LinException
import io.github.cafeteriaguild.lin.rt.lib.LObj
import java.util.concurrent.ConcurrentHashMap

class GlobalScope : Scope() {
    override val parent: Scope?
        get() = null

    operator fun set(name: String, value: LObj) {
        declareProperty(name, LocalProperty(false).also { it.set(value) })
    }

    val properties = ConcurrentHashMap<String, Property>()

    override fun findProperty(name: String): Property? {
        return properties[name]
    }

    override fun declareProperty(name: String, property: Property) {
        if (properties.putIfAbsent(name, property) != null) {
            throw LinException("Property $name already declared in your scope.")
        }
    }
}