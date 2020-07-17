package io.github.cafeteriaguild.lin.rt.lib

import io.github.cafeteriaguild.lin.rt.types.LType
import java.util.concurrent.ConcurrentHashMap

open class LObj(val type: LType) {
    val propertyValues = ConcurrentHashMap<String, LObj>()

}