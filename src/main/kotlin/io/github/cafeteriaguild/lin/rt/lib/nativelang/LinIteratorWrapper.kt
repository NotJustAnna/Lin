package io.github.cafeteriaguild.lin.rt.lib.nativelang

import io.github.cafeteriaguild.lin.rt.lib.LObj
import io.github.cafeteriaguild.lin.rt.lib.nativelang.routes.LinNativeIterator

class LinIteratorWrapper(val iterator: Iterator<LObj>) : LinNativeObj(), LinNativeIterator {
    init {
        declareIteratorFromNative()
        declareToString(this::toString)
        declareHashCode(this::hashCode)
        declareEquals<LinIteratorWrapper> { iterator == it.iterator }
    }

    override fun hasNext(): Boolean {
        return iterator.hasNext()
    }

    override fun next(): LObj {
        return iterator.next()
    }

    override fun toString(): String {
        return "iterator@${hashCode().toString(16)}"
    }

    override fun hashCode(): Int {
        return iterator.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        return iterator == (other as? LinIteratorWrapper ?: return false).iterator
    }
}