package io.github.cafeteriaguild.lin.rt

import io.github.cafeteriaguild.lin.rt.lib.LLong
import io.github.cafeteriaguild.lin.rt.lib.dsl.createLFun

object LinRuntime {
    val millis = createLFun { LLong(System.currentTimeMillis()) }
}