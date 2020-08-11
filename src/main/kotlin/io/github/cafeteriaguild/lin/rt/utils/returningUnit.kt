package io.github.cafeteriaguild.lin.rt.utils

import io.github.cafeteriaguild.lin.rt.lib.lang.LUnit

inline fun returningUnit(block: () -> Unit): LUnit {
    block()
    return LUnit
}
