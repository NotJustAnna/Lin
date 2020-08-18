package io.github.cafeteriaguild.lin.rt.lib.nativelang

import io.github.cafeteriaguild.lin.rt.exceptions.LinThrownException
import io.github.cafeteriaguild.lin.rt.lib.LObj
import io.github.cafeteriaguild.lin.rt.lib.lang.LBoolean
import io.github.cafeteriaguild.lin.rt.lib.lang.LString
import io.github.cafeteriaguild.lin.rt.lib.lang.number.LInt
import io.github.cafeteriaguild.lin.rt.lib.nativelang.properties.GetterProperty
import io.github.cafeteriaguild.lin.rt.lib.nativelang.properties.LazyProperty
import io.github.cafeteriaguild.lin.rt.lib.nativelang.properties.Property
import io.github.cafeteriaguild.lin.rt.lib.nativelang.properties.SimpleProperty
import io.github.cafeteriaguild.lin.rt.lib.nativelang.routes.*
import io.github.cafeteriaguild.lin.rt.utils.returningUnit
import java.util.concurrent.ConcurrentHashMap

abstract class LinNativeObj : AbstractObj() {
    protected val properties = ConcurrentHashMap<String, Property>()

    override fun properties(): Set<String> {
        return properties.keys
    }

    override fun propertyOf(name: String): Property? {
        return properties[name]
    }

    // utility methods below

    protected fun declareImmutableProperty(name: String): SimpleProperty {
        val p = SimpleProperty(false)
        properties[name] = p
        return p
    }

    protected fun declareMutableProperty(name: String): SimpleProperty {
        val p = SimpleProperty(true)
        properties[name] = p
        return p
    }

    protected fun lazyImmutableProperty(name: String, value: () -> LObj) {
        val p = LazyProperty(value)
        properties[name] = p
    }

    protected fun getterImmutableProperty(name: String, value: () -> LObj) {
        val p = GetterProperty(value)
        properties[name] = p
    }

    protected fun setImmutableProperty(name: String, value: LObj) {
        declareImmutableProperty(name).set(value)
    }

    protected fun setMutableProperty(name: String, value: LObj) {
        declareMutableProperty(name).set(value)
    }

    protected fun declareFunction(name: String, value: (List<LObj>) -> LObj) {
        val p = SimpleProperty(false)
        p.set(LinNativeFunction(toString() + "::" + name, value))
        properties[name] = p
    }

    protected inline fun declareToString(crossinline value: () -> String) {
        declareFunction("toString") { LString(value()) }
    }

    protected inline fun <reified R : LObj> declareEquals(crossinline value: (other: R) -> Boolean) {
        declareFunction("equals") {
            it.single().let { other -> if (other is R) LBoolean.of(value(other)) else LBoolean.FALSE }
        }
    }

    protected inline fun declareHashCode(crossinline value: () -> Int) {
        declareFunction("hashCode") { LInt(value()) }
    }

    protected inline fun declareIterator(crossinline value: () -> Iterator<LObj>) {
        declareFunction("iterator") {
            val iterator = value()
            iterator as? LObj ?: LinIteratorWrapper(iterator)
        }
    }

    protected fun LinNativeIterator.declareIteratorFromNative() {
        declareFunction("next") { this.next() }
        declareFunction("hasNext") { LBoolean.of(this.hasNext()) }
    }

    protected fun LinNativeIterable.declareIterableFromNative() {
        declareIterator { this.iterator() }
    }

    protected fun LinNativeGet.declareGetFromNative() {
        declareFunction("get", this::get)
    }

    protected fun LinNativeSet.declareSetFromNative() {
        declareFunction("set") { returningUnit { this[it.dropLast(1)] = it.last() } }
    }

    protected fun LinNativeDelegate.declareDelegateFromNative() {
        declareFunction("getValue") {
            val str = it.first() as? LString ?: throw LinThrownException("illegal_argument", "'name' is not a String")
            getValue(str.value)
        }
        if (this is LinNativeMutableDelegate) {
            declareFunction("setValue") {
                val str = it.first() as? LString
                    ?: throw LinThrownException("illegal_argument", "'name' is not a String")
                returningUnit { setValue(str.value, it[1]) }
            }
        }
    }

    protected fun LinNativeContains.declareContainsFromNative() {
        declareFunction("contains") {
            LBoolean.of(
                contains(
                    it.singleOrNull()
                        ?: throw LinThrownException("illegal_argument", "'contains' only receives a single argument")
                )
            )
        }
    }

    protected fun LinNativeRangeTo.declareRangeToFromNative() {
        declareFunction("rangeTo") {
            this.rangeTo(
                it.singleOrNull()
                    ?: throw LinThrownException("illegal_argument", "'rangeTo' only receives a single argument")
            )
        }
    }
}