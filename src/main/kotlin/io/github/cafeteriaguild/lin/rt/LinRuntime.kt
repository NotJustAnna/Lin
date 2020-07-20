package io.github.cafeteriaguild.lin.rt

import io.github.cafeteriaguild.lin.rt.exc.LinException
import io.github.cafeteriaguild.lin.rt.lib.LCallable
import io.github.cafeteriaguild.lin.rt.lib.LLong
import io.github.cafeteriaguild.lin.rt.lib.LString
import io.github.cafeteriaguild.lin.rt.lib.LUnit
import io.github.cafeteriaguild.lin.rt.lib.dsl.createGetter
import io.github.cafeteriaguild.lin.rt.lib.dsl.createLFun
import kotlin.concurrent.thread

object LinRuntime {
    val millis = createLFun { LLong(System.currentTimeMillis()) }
    val nanos = createLFun { LLong(System.nanoTime()) }
    val printlnConsole = createLFun {
        println(it.joinToString(" "))
        LUnit
    }
    val getThreadName = createGetter {
        LString(Thread.currentThread().name)
    }
    val runOnThread = createLFun {
        val callable = it.single() as? LCallable ?: throw LinException("$it is not callable.")
        thread {
            callable(emptyList())
        }
        LUnit
    }
}