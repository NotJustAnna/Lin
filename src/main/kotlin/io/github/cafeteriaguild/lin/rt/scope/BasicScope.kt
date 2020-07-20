package io.github.cafeteriaguild.lin.rt.scope

import io.github.cafeteriaguild.lin.rt.exc.LinException
import java.util.concurrent.ConcurrentHashMap

class BasicScope(override val parent: Scope? = null) : Scope() {
    val properties = ConcurrentHashMap<String, Property>()

    override fun findProperty(name: String): Property? {
        return properties[name] ?: super.findProperty(name)
    }

    override fun declareProperty(name: String, property: Property) {
        if (properties.putIfAbsent(name, property) != null) {
            throw LinException("Property $name already declared in your scope.")
        }
    }
}