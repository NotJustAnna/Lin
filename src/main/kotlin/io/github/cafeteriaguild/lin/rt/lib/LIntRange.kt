package io.github.cafeteriaguild.lin.rt.lib

import io.github.cafeteriaguild.lin.rt.exc.LinException
import io.github.cafeteriaguild.lin.rt.lib.dsl.createGetter

data class LIntRange(val range: IntProgression) : LIterable, LRangeTo {
    private val getFirst = createGetter { LInt(range.first) }
    private val getLast = createGetter { LInt(range.last) }
    private val getStep = createGetter { LInt(range.step) }

    override fun rangeTo(other: LObj): LObj {
        return LIntRange(
            range step (other as? LNumber ?: throw LinException("$other is not a Number")).value.toInt()
        )
    }

    override fun property(name: String) = when (name) {
        "first", "start" -> getFirst
        "last", "endInclusive" -> getLast
        "step" -> getStep
        else -> null
    }

    override fun toString() = range.toString()

    override fun iterator(): Iterator<LObj> {
        return range.asSequence().map(::LInt).iterator()
    }
}