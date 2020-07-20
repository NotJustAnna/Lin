package io.github.cafeteriaguild.lin.oldrt.lib

import io.github.cafeteriaguild.lin.oldrt.types.LTAny

object LNull : LObj(LTAny) {
    override fun toString() = "null"
}