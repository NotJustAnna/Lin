package io.github.cafeteriaguild.lin.rt.scope

import io.github.cafeteriaguild.lin.rt.exc.LinException

abstract class Scope {
    protected abstract val parent: Scope?

    open fun findProperty(name: String): Property? {
        return parent?.findProperty(name)
    }

    open fun declareProperty(name: String, property: Property) {
        parent?.declareProperty(name, property) ?: throw LinException(
            "Property declaration isn't supported."
        )
    }
}