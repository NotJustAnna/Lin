package com.github.adriantodt.lin.vm.types

data class LString(val value: String) : LAny() {
    override fun truth(): Boolean {
        return value.isNotEmpty()
    }

    override val linType: String
        get() = "string"

    override fun getMember(name: String): LAny? {
        return null
    }

    override fun toString(): String {
        return value
    }
}
