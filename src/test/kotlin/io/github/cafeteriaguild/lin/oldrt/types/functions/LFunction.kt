package io.github.cafeteriaguild.lin.oldrt.types.functions

import io.github.cafeteriaguild.lin.rt.lib.LObj

/**
 * Represents a Lin function
 */
interface LFunction {
    fun call(receiver: LObj, params: List<LObj>): LObj
}