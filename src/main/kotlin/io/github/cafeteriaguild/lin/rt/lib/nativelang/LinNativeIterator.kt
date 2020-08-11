package io.github.cafeteriaguild.lin.rt.lib.nativelang

import io.github.cafeteriaguild.lin.rt.lib.LObj
import io.github.cafeteriaguild.lin.rt.lib.lang.LBoolean
import io.github.cafeteriaguild.lin.rt.lib.lang.LString

class LinNativeIterator(val iterator: Iterator<LObj>) : LinNativeObj() {
    init {
        lazyImmutableProperty("toString") {
            LinNativeFunction("toString") {
                LString("Iterator(hasNext: ${iterator.hasNext()})")
            }
        }
        lazyImmutableProperty("next") { LinNativeFunction("next") { iterator.next() } }
        lazyImmutableProperty("hasNext") { LinNativeFunction("hasNext") { LBoolean.of(iterator.hasNext()) } }
    }
}