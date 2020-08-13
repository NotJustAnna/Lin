package io.github.cafeteriaguild.lin.rt.lib.lang

import io.github.cafeteriaguild.lin.rt.lib.LObj

enum class LBoolean(val value: Boolean) : LObj {
    TRUE(true) {
        override fun not() = FALSE
    },
    FALSE(false) {
        override fun not() = TRUE
    };

    override fun toString() = value.toString()
    abstract operator fun not(): LBoolean

    companion object {
        fun of(value: Boolean) = if (value) TRUE else FALSE
    }
}