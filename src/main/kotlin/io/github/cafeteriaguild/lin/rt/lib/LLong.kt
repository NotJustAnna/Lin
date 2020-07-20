package io.github.cafeteriaguild.lin.rt.lib

import io.github.cafeteriaguild.lin.rt.exc.LinException

data class LLong(override val value: Long) : LNumber, LRangeTo {
    override fun rangeTo(other: LObj): LObj {
        return LLongRange(
            value..(other as? LNumber ?: throw LinException("$other is not a Number")).value.toLong()
        )
    }

    override fun toString() = value.toString()
}