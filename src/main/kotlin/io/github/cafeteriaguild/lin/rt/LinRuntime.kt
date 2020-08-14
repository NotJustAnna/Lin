package io.github.cafeteriaguild.lin.rt

import io.github.cafeteriaguild.lin.rt.exceptions.LinException
import io.github.cafeteriaguild.lin.rt.lib.lang.LString
import io.github.cafeteriaguild.lin.rt.lib.lang.LUnit
import io.github.cafeteriaguild.lin.rt.lib.lang.number.LLong
import io.github.cafeteriaguild.lin.rt.lib.nativelang.LinNativeFunction
import io.github.cafeteriaguild.lin.rt.utils.returningUnit
import kotlin.concurrent.thread

object LinRuntime {
    val millis = LinNativeFunction { LLong(System.currentTimeMillis()) }
    val nanos = LinNativeFunction { LLong(System.nanoTime()) }
    val printlnConsole = LinNativeFunction {
        println(it.joinToString(" "))
        LUnit
    }
    val threadName = LinNativeFunction { LString(Thread.currentThread().name) }
    val runOnThread = LinNativeFunction {
        returningUnit {
            val callable = it.single()
            if (!callable.canInvoke()) throw LinException("$it is not callable.")
            thread { callable.callable()(emptyList()) }
        }
    }
}