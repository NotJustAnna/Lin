package io.github.cafeteriaguild.lin.rt.lib

import io.github.cafeteriaguild.lin.rt.exc.LinException
import io.github.cafeteriaguild.lin.rt.lib.dsl.createGetter

data class LLongRange(val range: LongProgression) : LIterable, LRangeTo {
    private val getFirst = createGetter { LLong(range.first) }
    private val getLast = createGetter { LLong(range.last) }
    private val getStep = createGetter { LLong(range.step) }

    override fun rangeTo(other: LObj): LObj {
        return LLongRange(
            range step (other as? LNumber ?: throw LinException("$other is not a Number")).value.toLong()
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
        return range.asSequence().map(::LLong).iterator()
    }
}