package io.github.cafeteriaguild.lin.rt.lib.nativelang.routes

import io.github.cafeteriaguild.lin.rt.LinInterpreter
import io.github.cafeteriaguild.lin.rt.lib.LObj

/**
 * Allows a direct route to be able to reuse the interpreter.
 */
interface LinDirectCall {
    fun call(interpreter: LinInterpreter, args: List<LObj> = emptyList()): LObj
}