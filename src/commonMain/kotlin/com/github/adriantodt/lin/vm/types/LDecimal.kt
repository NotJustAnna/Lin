package com.github.adriantodt.lin.vm.types

data class LDecimal(val value: Double) : LAny() {
    override fun truth(): Boolean {
        return value != 0.0
    }

    override val linType: String
        get() = "decimal"

    override fun getMember(name: String): LAny? {
        return null
    }

    override fun toString(): String {
        return value.toString()
    }
}
