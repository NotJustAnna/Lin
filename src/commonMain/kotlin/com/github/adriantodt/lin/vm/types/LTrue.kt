package com.github.adriantodt.lin.vm.types

object LTrue : LAny() {
    override fun truth(): Boolean {
        return true
    }

    override val linType: String
        get() = "boolean"

    override fun getMember(name: String): LAny? {
        return null
    }

    override fun toString(): String {
        return "true"
    }
}
