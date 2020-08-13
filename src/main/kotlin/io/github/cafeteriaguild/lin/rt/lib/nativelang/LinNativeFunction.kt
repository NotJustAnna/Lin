package io.github.cafeteriaguild.lin.rt.lib.nativelang

import io.github.cafeteriaguild.lin.rt.lib.LObj
import io.github.cafeteriaguild.lin.rt.lib.lang.LString
import io.github.cafeteriaguild.lin.rt.lib.nativelang.invoke.LinCall

class LinNativeFunction(val name: String = "<native function>", val block: (List<LObj>) -> LObj) : LinNativeObj(), LinCall {
    init {
        lazyImmutableProperty("toString") { LinNativeFunction("toString") { LString(name) } }
        lazyImmutableProperty("invoke") { this }
    }

    override fun canInvoke() = true

    override fun invoke(args: List<LObj>) = block(args)
}