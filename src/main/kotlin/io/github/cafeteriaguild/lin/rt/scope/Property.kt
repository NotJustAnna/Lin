package io.github.cafeteriaguild.lin.rt.scope

import io.github.cafeteriaguild.lin.rt.lib.LObj

interface Property {
    val getAllowed: Boolean
    val setAllowed: Boolean
    fun get(): LObj
    fun set(value: LObj)
}