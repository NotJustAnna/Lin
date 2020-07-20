package io.github.cafeteriaguild.lin.oldrt.coroutines

import io.github.cafeteriaguild.lin.oldrt.LinThrowable
import io.github.cafeteriaguild.lin.oldrt.coroutines.LinResult.Companion.supplier
import io.github.cafeteriaguild.lin.rt.lib.LObj

sealed class LinContinuation {
    abstract fun hasNext(): Boolean
    abstract fun next(): LinContinuation
    open fun result(): LinResult {
        throw IllegalStateException("Continuation is not done.")
    }

    open val isReturn: Boolean
        get() = false


    open fun returning(): LinContinuation {
        throw IllegalStateException("Continuation is not done.")
    }

    open fun then(continuation: LinContinuation): LinContinuation {
        return Statements(
            listOf(
                this,
                continuation
            )
        )
    }

    fun with(block: (LinResult) -> LinContinuation): LinContinuation {
        return Combine(this, block)
    }

    class Compute(private val block: () -> LinContinuation) : LinContinuation() {
        override fun hasNext() = true
        override fun next() = block()

        override fun toString() = "Continuation(... -> empty)"
    }

    class Combine(private val initial: LinContinuation, private val block: (LinResult) -> LinContinuation) :
        LinContinuation() {
        override fun hasNext() = true
        override fun next() = when {
            initial.hasNext() -> Combine(
                initial.next(),
                block
            )
            initial.isReturn -> initial
            else -> Compute { block(initial.result()) }
        }

        override fun toString() = "Continuation($initial, ... -> continuation)"
    }

    class Done(val result: LinResult, override val isReturn: Boolean = false) : LinContinuation() {
        override fun hasNext() = false
        override fun next() = this
        override fun result() = result

        override fun toString() = "Continuation($result)"

        override fun returning(): LinContinuation {
            return Done(result, true)
        }
    }

    @OptIn(ExperimentalStdlibApi::class)
    class Statements(list: List<LinContinuation>) : LinContinuation() {
        val list = ArrayList(list)
        var current: LinContinuation? = null
        override var isReturn: Boolean = false

        override fun hasNext() = true

        override fun next(): LinContinuation {
            current = (current ?: list.removeFirstOrNull() ?: return empty()).let {
                when {
                    it.hasNext() -> it.next()
                    it.isReturn || it.result() is Throwable -> return it
                    list.isNotEmpty() -> list.removeFirst()
                    else -> return empty()
                }
            }
            return this
        }

        override fun then(continuation: LinContinuation): LinContinuation {
            list.add(continuation)
            return this
        }

        override fun returning(): LinContinuation {
            isReturn = true
            return this
        }
    }

    companion object {
        fun supplying(block: () -> LinResult): LinContinuation {
            return Compute { Done(block()) }
        }

        fun applying(block: (LinResult) -> LinResult): (LinResult) -> LinContinuation {
            return { Done(block(it)) }
        }

        fun compute(block: () -> LObj): LinContinuation = supplying(supplier(block))
        fun empty() = Done(LinResult.Empty)
        fun of(obj: LObj): LinContinuation = Done(LinResult.Value(obj))
        fun ofError(t: LinThrowable): LinContinuation = Done(LinResult.Throwable(t))
    }
}


//    class Compute(private val block: () -> Result) : Continuation() {
//        override fun hasNext() = true
//        override fun next() = Done(block())
//
//        override fun toString() = "Continuation(... -> empty)"
//    }

//    class Combine(private val initial: Continuation, private val block: (Result) -> Result) : Continuation() {
//        override fun hasNext() = true
//        override fun next() = when {
//            initial.hasNext() -> Combine(initial.next(), block)
//            initial.isReturn -> initial
//            else -> Compute { block(initial.result()) }
//        }
//
//        override fun toString() = "Continuation($initial, ... -> continuation)"
//    }
