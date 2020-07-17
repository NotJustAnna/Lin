package io.github.cafeteriaguild.lin.rt.coroutines

import io.github.cafeteriaguild.lin.rt.coroutines.LinContinuation.Companion.applying
import io.github.cafeteriaguild.lin.rt.coroutines.LinContinuation.Companion.supplying
import io.github.cafeteriaguild.lin.rt.coroutines.LinResult.Companion.consumer
import io.github.cafeteriaguild.lin.rt.coroutines.LinResult.Companion.function
import io.github.cafeteriaguild.lin.rt.coroutines.LinResult.Companion.runnable
import io.github.cafeteriaguild.lin.rt.lib.LObj

fun LinContinuation.thenRun(block: () -> Unit) = then(supplying(runnable(block)))

fun LinContinuation.acceptWith(block: (LObj) -> Unit) = with(applying(consumer(block)))

fun LinContinuation.applyWith(block: (LObj) -> LObj) = with(applying(function(block)))