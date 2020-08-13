package io.github.cafeteriaguild.lin.rt.lib.nativelang.routes

import io.github.cafeteriaguild.lin.rt.lib.LObj

interface LinNativeDelegate {
    fun getValue(propertyName: String): LObj
}