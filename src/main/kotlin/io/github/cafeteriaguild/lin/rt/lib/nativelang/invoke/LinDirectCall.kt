package io.github.cafeteriaguild.lin.rt.lib.nativelang.invoke

import io.github.cafeteriaguild.lin.rt.LinInterpreter
import io.github.cafeteriaguild.lin.rt.lib.LObj

/**
 * Allows a direct route to be able to reuse the interpreter.
 */
interface LinDirectCall : LinCall {
    fun call(interpreter: LinInterpreter, args: List<LObj> = emptyList()): LObj
}