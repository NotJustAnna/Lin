package io.github.cafeteriaguild.lin.rt.lib.dsl

import io.github.cafeteriaguild.lin.rt.lib.LCallable
import io.github.cafeteriaguild.lin.rt.lib.LObj
import io.github.cafeteriaguild.lin.rt.scope.LocalProperty
import io.github.cafeteriaguild.lin.rt.scope.Property

class LObjBuilder {
    private val properties = LinkedHashMap<String, Property>()
    private var component: (Int) -> LObj? = { null }
    private var toString: (LObj) -> String = { it.toString() }
    private var call: ((List<LObj>) -> LObj)? = null

    fun toString(block: (LObj) -> String) {
        toString = block
    }

    fun component(block: (Int) -> LObj?) {
        component = block
    }

    fun getOrCreateProperty(name: String): Property {
        return properties.getOrPut(name) { LocalProperty(false) }
    }

    fun getOrCreateMutableProperty(name: String): Property {
        return properties.getOrPut(name) { LocalProperty(true) }
    }

    fun call(block: ((List<LObj>) -> LObj)?) {
        call = block
    }

    internal fun build(): LObj {
        return if (call != null) Callable() else Obj()
    }

    private open inner class Obj : LObj {
        override fun toString() = toString.invoke(this)
        override fun component(value: Int) = component.invoke(value)
        override fun property(name: String) = properties[name]
    }

    private inner class Callable : Obj(), LCallable {
        override fun invoke(args: List<LObj>) = call!!.invoke(args)
    }
}

fun createLObj(block: LObjBuilder.() -> Unit): LObj {
    return LObjBuilder().also(block).build()
}

fun createLFun(block: (List<LObj>) -> LObj): LObj = createLObj {
    toString { "<native function>" }
    call(block)
}
