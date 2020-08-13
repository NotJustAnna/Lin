package io.github.cafeteriaguild.lin.rt.lib.nativelang.routes

import io.github.cafeteriaguild.lin.rt.lib.LObj

interface LinNativeMutableDelegate : LinNativeDelegate {
    fun setValue(propertyName: String, value: LObj)
}