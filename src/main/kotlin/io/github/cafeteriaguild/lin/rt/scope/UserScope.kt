package io.github.cafeteriaguild.lin.rt.scope

import io.github.cafeteriaguild.lin.rt.exceptions.LinException
import io.github.cafeteriaguild.lin.rt.lib.LObj
import io.github.cafeteriaguild.lin.rt.lib.nativelang.properties.Property
import io.github.cafeteriaguild.lin.rt.lib.nativelang.properties.SimpleProperty
import java.util.concurrent.ConcurrentHashMap

class UserScope(override val parent: Scope? = null) : Scope() {

    operator fun set(name: String, value: LObj) {
        declareProperty(name, SimpleProperty(false).also { it.set(value) })
    }

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