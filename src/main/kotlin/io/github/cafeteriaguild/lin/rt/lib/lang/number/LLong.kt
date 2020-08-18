package io.github.cafeteriaguild.lin.rt.lib.lang.number

import io.github.cafeteriaguild.lin.rt.exceptions.LinException
import io.github.cafeteriaguild.lin.rt.lib.LObj
import io.github.cafeteriaguild.lin.rt.lib.lang.ranges.LLongRange
import io.github.cafeteriaguild.lin.rt.lib.nativelang.LinNativeObj
import io.github.cafeteriaguild.lin.rt.lib.nativelang.routes.LinNativeRangeTo

data class LLong(override val value: Long) : LinNativeObj(), LNumber, LinNativeRangeTo {
    init {
        declareRangeToFromNative()
    }

    override fun rangeTo(other: LObj): LObj {
        return LLongRange(
            value..(other as? LNumber
                ?: throw LinException("$other is not a Number")).value.toLong()
        )
    }

    override fun toString() = value.toString()
}