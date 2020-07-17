package io.github.cafeteriaguild.lin.rt.lib

import io.github.cafeteriaguild.lin.rt.types.LTAny

object LNull : LObj(LTAny) {
    override fun toString() = "null"
}