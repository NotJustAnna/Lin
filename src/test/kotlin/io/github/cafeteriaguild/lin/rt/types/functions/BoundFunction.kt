package io.github.cafeteriaguild.lin.rt.types.functions

import io.github.cafeteriaguild.lin.rt.lib.LObj

class BoundFunction(val function: LFunction, val target: LObj) : LFunction {
    override fun call(receiver: LObj, params: List<LObj>): LObj {
        return function.call(target, params)
    }
}
