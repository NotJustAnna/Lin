package io.github.cafeteriaguild.lin.rt.lib.nativelang.routes

import io.github.cafeteriaguild.lin.rt.lib.LObj

/**
 * Optimization interface. This route overrides usual behaviour.
 */
interface LinNativeContains {
    operator fun contains(obj: LObj): Boolean
}

