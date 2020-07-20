package io.github.cafeteriaguild.lin.oldrt.lib

import io.github.cafeteriaguild.lin.oldrt.types.LTAny
import io.github.cafeteriaguild.lin.rt.lib.LObj

object LNull : LObj(LTAny) {
    override fun toString() = "null"
}