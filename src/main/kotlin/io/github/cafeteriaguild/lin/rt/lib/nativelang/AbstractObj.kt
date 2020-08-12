package io.github.cafeteriaguild.lin.rt.lib.nativelang

import io.github.cafeteriaguild.lin.rt.lib.LObj
import io.github.cafeteriaguild.lin.rt.lib.lang.LBoolean
import io.github.cafeteriaguild.lin.rt.lib.lang.LString
import io.github.cafeteriaguild.lin.rt.lib.lang.number.LInt

abstract class AbstractObj : LObj {
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
        return "object@${hashCode().toString(16)}"
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
}