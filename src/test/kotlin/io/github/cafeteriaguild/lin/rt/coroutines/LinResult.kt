package io.github.cafeteriaguild.lin.rt.coroutines

import io.github.cafeteriaguild.lin.rt.lib.LObj
import io.github.cafeteriaguild.lin.rt.lib.LUnit

sealed class LinResult {
    object Empty : LinResult() {
        override fun toString() = "Unit"
    }

    class Value(val value: LObj) : LinResult() {
        override fun toString() = "Value($value)"
    }

    class Throwable(val throwable: _root_ide_package_.io.github.cafeteriaguild.lin.rt.LinThrowable) : LinResult() {
        override fun toString() = "Throwable($throwable)"
    }

    fun unwrap(): LObj? = when (this) {
        Empty -> null
        is Value -> value
        is Throwable -> throw throwable
    }

    fun unwrapOrUnit(): LObj = when (this) {
        Empty -> LUnit
        is Value -> value
        is Throwable -> throw throwable
    }

    companion object {
        fun runnable(block: () -> Unit): () -> LinResult = {
            try {
                block()
                Empty
            } catch (t: _root_ide_package_.io.github.cafeteriaguild.lin.rt.LinThrowable) {
                Throwable(t)
            }
        }

        fun supplier(block: () -> LObj): () -> LinResult = {
            try {
                Value(block())
            } catch (t: _root_ide_package_.io.github.cafeteriaguild.lin.rt.LinThrowable) {
                Throwable(t)
            }
        }

        fun consumer(block: (LObj) -> Unit): (LinResult) -> LinResult = {
            try {
                block(it.unwrap() ?: error("Result is empty."))
                Empty
            } catch (t: _root_ide_package_.io.github.cafeteriaguild.lin.rt.LinThrowable) {
                Throwable(t)
            }
        }

        fun function(block: (LObj) -> LObj): (LinResult) -> LinResult = {
            try {
                Value(block(it.unwrap() ?: error("Result is empty.")))
            } catch (t: _root_ide_package_.io.github.cafeteriaguild.lin.rt.LinThrowable) {
                Throwable(t)
            }
        }
    }
}