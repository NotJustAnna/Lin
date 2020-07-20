package io.github.cafeteriaguild.lin.rt.lib

import io.github.cafeteriaguild.lin.rt.scope.Property

interface LObj {
    fun property(name: String): Property? = null
    fun component(value: Int): LObj? = null
}
