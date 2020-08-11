package io.github.cafeteriaguild.lin.rt.lib.lang.ranges

import io.github.cafeteriaguild.lin.rt.exceptions.LinException
import io.github.cafeteriaguild.lin.rt.lib.LObj
import io.github.cafeteriaguild.lin.rt.lib.lang.number.LInt
import io.github.cafeteriaguild.lin.rt.lib.lang.number.LNumber
import io.github.cafeteriaguild.lin.rt.lib.nativelang.LinNativeObj
import io.github.cafeteriaguild.lin.rt.lib.nativelang.routes.LinNativeIterable
import io.github.cafeteriaguild.lin.rt.lib.nativelang.routes.LinNativeRangeTo

data class LIntRange(val range: IntProgression) : LinNativeObj(), LinNativeIterable, LinNativeRangeTo {
    init {
        lazyImmutableProperty("first") { LInt(range.first) }
        lazyImmutableProperty("last") { LInt(range.last) }
        lazyImmutableProperty("step") { LInt(range.step) }
        declareFunction("rangeTo") { rangeTo(it.single()) }
        declareIterator(this::iterator)
        declareToString(range::toString)
        declareHashCode(range::hashCode)
        declareEquals<LIntRange> { range == it.range }
    }

    override fun toString() = range.toString()

    override fun iterator(): Iterator<LObj> {
        return range.asSequence().map(::LInt).iterator()
    }

    override fun rangeTo(other: LObj): LObj {
        return LIntRange(
            range step (other as? LNumber
                ?: throw LinException("$other is not a Number")).value.toInt()
        )
    }
}