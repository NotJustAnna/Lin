package io.github.cafeteriaguild.lin.oldrt.lib

import io.github.cafeteriaguild.lin.oldrt.types.LType
import java.util.concurrent.ConcurrentHashMap

open class LObj(val type: LType) {
    val propertyValues = ConcurrentHashMap<String, LObj>()

}