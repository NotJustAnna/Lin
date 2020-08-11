package io.github.cafeteriaguild.lin.rt.lib.lang.ranges

import io.github.cafeteriaguild.lin.rt.exceptions.LinException
import io.github.cafeteriaguild.lin.rt.lib.LObj
import io.github.cafeteriaguild.lin.rt.lib.lang.number.LLong
import io.github.cafeteriaguild.lin.rt.lib.lang.number.LNumber
import io.github.cafeteriaguild.lin.rt.lib.nativelang.LinNativeIterator
import io.github.cafeteriaguild.lin.rt.lib.nativelang.LinNativeObj
import io.github.cafeteriaguild.lin.rt.lib.nativelang.routes.LinNativeIterable
import io.github.cafeteriaguild.lin.rt.lib.nativelang.routes.LinNativeRangeTo

data class LLongRange(val range: LongProgression) : LinNativeObj(), LinNativeIterable, LinNativeRangeTo {
    init {
        lazyImmutableProperty("first") { LLong(range.first) }
        lazyImmutableProperty("last") { LLong(range.last) }
        lazyImmutableProperty("step") { LLong(range.step) }
        declareFunction("rangeTo") { rangeTo(it.single()) }
        declareFunction("iterator") { LinNativeIterator(iterator()) }
        declareToString(range::toString)
        declareHashCode(range::hashCode)
        declareEquals<LLongRange> { range == it.range }
    }

    override fun rangeTo(other: LObj): LObj {
        return LLongRange(
            range step (other as? LNumber
                ?: throw LinException("$other is not a Number")).value.toLong()
        )
    }

    override fun toString() = range.toString()

    override fun iterator(): Iterator<LObj> {
        return range.asSequence().map(::LLong).iterator()
    }
}