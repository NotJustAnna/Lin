package io.github.cafeteriaguild.lin.oldrt.types

import io.github.cafeteriaguild.lin.oldrt.lib.LObj
import io.github.cafeteriaguild.lin.oldrt.types.functions.LFunction

object LTAny : LTClass.Base() {
    override val constructors = listOf<LFunction>(object : LFunction {
        override fun call(receiver: LObj, params: List<LObj>): LObj =
            LObj(LTAny)
    })
    override val name = "lin.Any"
}