package io.github.cafeteriaguild.lin.rt.lib

import io.github.cafeteriaguild.lin.rt.exc.LinException

data class LInt(override val value: Int) : LNumber, LRangeTo {
    override fun rangeTo(other: LObj): LObj {
        return LIntRange(
            value..(other as? LNumber ?: throw LinException("$other is not a Number")).value.toInt()
        )
    }

    override fun toString() = value.toString()
}