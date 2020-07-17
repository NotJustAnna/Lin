package io.github.cafeteriaguild.lin.rt.scope

import io.github.cafeteriaguild.lin.rt.types.LType
import io.github.cafeteriaguild.lin.rt.types.functions.LFunction
import io.github.cafeteriaguild.lin.rt.types.properties.LObjectProperty
import io.github.cafeteriaguild.lin.rt.types.properties.LProperty

interface Scope {
    fun findProperty(name: String): LProperty

    fun declareLocalProperty(name: String, mutable: Boolean): LProperty

    fun findLocalFunction(name: String): LFunction

    fun findExtensionProperty(type: LType, name: String): LObjectProperty

    fun findExtensionFunctions(type: LType, name: String): LFunction
}

