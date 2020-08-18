package io.github.cafeteriaguild.lin.rt.lib.lang.collections

import io.github.cafeteriaguild.lin.rt.lib.LObj
import io.github.cafeteriaguild.lin.rt.lib.lang.LNull
import io.github.cafeteriaguild.lin.rt.lib.lang.number.LInt
import io.github.cafeteriaguild.lin.rt.lib.lang.number.LNumber
import io.github.cafeteriaguild.lin.rt.lib.nativelang.LinNativeObj
import io.github.cafeteriaguild.lin.rt.lib.nativelang.routes.LinNativeContains
import io.github.cafeteriaguild.lin.rt.lib.nativelang.routes.LinNativeGet
import io.github.cafeteriaguild.lin.rt.lib.nativelang.routes.LinNativeIterable
import io.github.cafeteriaguild.lin.rt.lib.nativelang.routes.LinNativeSet

class LList(list: List<LObj>) : LinNativeObj(), LinNativeGet, LinNativeSet, LinNativeIterable, LinNativeContains {
    val list: MutableList<LObj> = list.toMutableList()

    init {
        getterImmutableProperty("size") { LInt(list.size) }
        declareGetFromNative()
        declareSetFromNative()
        declareIterableFromNative()
        declareContainsFromNative()
        declareToString(list::toString)
        declareHashCode(list::hashCode)
        declareEquals<LList> { list == it.list }
    }

    override fun contains(obj: LObj): Boolean {
        return obj in list
    }

    override fun get(args: List<LObj>): LObj {
        return list.getOrNull((args.single() as LNumber).value.toInt()) ?: LNull
    }

    override fun set(args: List<LObj>, value: LObj) {
        list[(args.single() as LNumber).value.toInt()] = value
    }

    override fun iterator(): Iterator<LObj> {
        return list.iterator()
    }

    override fun toString(): String {
        return list.toString()
    }

    override fun hashCode(): Int {
        return list.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        return list == (other as? LList ?: return false).list
    }
}