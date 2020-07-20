package io.github.cafeteriaguild.lin.oldrt.lib

import io.github.cafeteriaguild.lin.oldrt.types.LType
import io.github.cafeteriaguild.lin.rt.lib.LObj
import java.util.concurrent.ConcurrentHashMap

open class LObj(val type: LType) {
    val propertyValues = ConcurrentHashMap<String, LObj>()

}