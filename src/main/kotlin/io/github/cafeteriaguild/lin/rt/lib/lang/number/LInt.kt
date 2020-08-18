package io.github.cafeteriaguild.lin.rt.lib.lang.number

import io.github.cafeteriaguild.lin.rt.exceptions.LinException
import io.github.cafeteriaguild.lin.rt.lib.LObj
import io.github.cafeteriaguild.lin.rt.lib.lang.ranges.LIntRange
import io.github.cafeteriaguild.lin.rt.lib.nativelang.LinNativeObj
import io.github.cafeteriaguild.lin.rt.lib.nativelang.routes.LinNativeRangeTo

data class LInt(override val value: Int) : LinNativeObj(), LNumber, LinNativeRangeTo {
    init {
        declareRangeToFromNative()
    }

    override fun rangeTo(other: LObj): LObj {
        return LIntRange(
            value..(other as? LNumber
                ?: throw LinException("$other is not a Number")).value.toInt()
        )
    }

    override fun toString() = value.toString()
}