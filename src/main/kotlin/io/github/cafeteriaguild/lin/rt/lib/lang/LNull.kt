package io.github.cafeteriaguild.lin.rt.lib.lang

import io.github.cafeteriaguild.lin.rt.lib.LObj

object LNull : LObj {
    override fun propertyOf(name: String): Nothing? = null

    override fun toString() = "null"
}