package io.github.cafeteriaguild.lin.oldrt.lib

import io.github.cafeteriaguild.lin.oldrt.types.LTClass
import io.github.cafeteriaguild.lin.rt.lib.LObj

object LUnit : LObj(LTClass.Impl("lin.Unit")) {
    override fun toString() = "Unit"
}