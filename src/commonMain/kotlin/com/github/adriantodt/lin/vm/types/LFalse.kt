package com.github.adriantodt.lin.vm.types

object LFalse : LAny() {
    override fun truth(): Boolean {
        return false
    }

    override val linType: String
        get() = "boolean"

    override fun getMember(name: String): LAny? {
        return null
    }

    override fun toString(): String {
        return "false"
    }
}
