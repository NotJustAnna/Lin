package io.github.cafeteriaguild.lin.rt.lib.nativelang.routes

import io.github.cafeteriaguild.lin.rt.lib.LObj

/**
 * Optimization interface. This route overrides usual behaviour.
 */
interface LinNativeRangeTo {
    operator fun rangeTo(other: LObj): LObj
}