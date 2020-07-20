package io.github.cafeteriaguild.lin.oldrt.types.functions

import io.github.cafeteriaguild.lin.oldrt.lib.LObj

class BoundFunction(val function: LFunction, val target: LObj) : LFunction {
    override fun call(receiver: LObj, params: List<LObj>): LObj {
        return function.call(target, params)
    }
}
