package io.github.cafeteriaguild.lin.rt.types

import io.github.cafeteriaguild.lin.rt.lib.LObj
import io.github.cafeteriaguild.lin.rt.types.functions.LFunction

object LTAny : LTClass.Base() {
    override val constructors = listOf<LFunction>(object : LFunction {
        override fun call(receiver: LObj, params: List<LObj>): LObj = LObj(LTAny)
    })
    override val name = "lin.Any"
}