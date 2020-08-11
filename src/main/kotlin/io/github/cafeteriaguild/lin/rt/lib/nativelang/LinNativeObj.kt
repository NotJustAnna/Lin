package io.github.cafeteriaguild.lin.rt.lib.nativelang

import io.github.cafeteriaguild.lin.rt.lib.LObj
import io.github.cafeteriaguild.lin.rt.lib.lang.LBoolean
import io.github.cafeteriaguild.lin.rt.lib.lang.LString
import io.github.cafeteriaguild.lin.rt.lib.lang.number.LInt
import io.github.cafeteriaguild.lin.rt.lib.nativelang.properties.GetterProperty
import io.github.cafeteriaguild.lin.rt.lib.nativelang.properties.LazyProperty
import io.github.cafeteriaguild.lin.rt.lib.nativelang.properties.Property
import io.github.cafeteriaguild.lin.rt.lib.nativelang.properties.SimpleProperty
import java.util.concurrent.ConcurrentHashMap

abstract class LinNativeObj : LObj {
    protected val properties = ConcurrentHashMap<String, Property>()

    override fun properties(): Set<String> {
        return properties.keys
    }

    override fun propertyOf(name: String): Property? {
        return properties[name]
    }

    override fun canGet(name: String): Boolean {
        return propertyOf(name)?.getAllowed ?: false
    }

    override fun get(name: String): LObj {
        return propertyOf(name)!!.get()
    }

    override fun canSet(name: String): Boolean {
        return propertyOf(name)?.setAllowed ?: false
    }

    override fun set(name: String, value: LObj) {
        propertyOf(name)!!.set(value)
    }

    override fun toString(): String {
        if (canGet("toString")) {
            val toStringFn = get("toString")
            if (toStringFn.canInvoke()) {
                val result = toStringFn.invoke()
                if (result is LString) {
                    return result.value
                }
            }
        }
        return "Lin object"
    }

    override fun hashCode(): Int {
        if (canGet("hashCode")) {
            val hashCodeFn = get("hashCode")
            if (hashCodeFn.canInvoke()) {
                val result = hashCodeFn.invoke()
                if (result is LInt) {
                    return result.value
                }
            }
        }
        return super.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (other !is LObj) return false
        if (canGet("equals")) {
            val equalsFn = get("equals")
            if (equalsFn.canInvoke()) {
                val result = equalsFn.invoke(listOf(other))
                if (result is LBoolean) {
                    return result.value
                }
            }
        }
        return super.equals(other)
    }

    // utility methods below

    protected fun declareImmutableProperty(name: String): SimpleProperty {
        val p = SimpleProperty(false)
        properties[name] = p
        return p
    }

    protected fun declareMutableProperty(name: String): SimpleProperty {
        val p = SimpleProperty(false)
        properties[name] = p
        return p
    }

    protected fun setImmutableProperty(name: String, value: LObj) {
        val p = SimpleProperty(false)
        p.set(value)
        properties[name] = p
    }

    protected fun lazyImmutableProperty(name: String, value: () -> LObj) {
        val p = LazyProperty(value)
        properties[name] = p
    }

    protected fun getterImmutableProperty(name: String, value: () -> LObj) {
        val p = GetterProperty(value)
        properties[name] = p
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
        declareFunction("iterator") { LinNativeIterator(value()) }
    }
}