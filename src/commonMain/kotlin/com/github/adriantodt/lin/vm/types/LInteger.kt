package com.github.adriantodt.lin.vm.types

data class LInteger(val value: Long) : LAny() {
    override fun truth(): Boolean {
        return value != 0L
    }

    override val linType: String
        get() = "integer"

    override fun getMember(name: String): LAny? {
        return null
    }

    override fun toString(): String {
        return value.toString()
    }
}

