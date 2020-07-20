package io.github.cafeteriaguild.lin.rt.lib

enum class LBoolean(val value: Boolean) : LObj {
    TRUE(true) {
        override fun not() = FALSE
    },
    FALSE(false) {
        override fun not() = TRUE
    };

    override fun toString() = value.toString()
    abstract fun not(): LBoolean

    companion object {
        fun valueOf(value: Boolean) = if (value) TRUE else FALSE
    }
}