package io.github.cafeteriaguild.lin.rt.lib

import io.github.cafeteriaguild.lin.rt.types.LTClass

object LUnit : LObj(LTClass.Impl("lin.Unit")) {
    override fun toString() = "Unit"
}