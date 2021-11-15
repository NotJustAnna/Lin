package com.github.adriantodt.lin.vm.types

public sealed class LFunction : LAny() {
    override fun truth(): Boolean {
        return true
    }

    override val linType: String
        get() = "function"

    public open operator fun invoke(vararg args: LAny): LAny {
        return call(null, args.toList())
    }

    public abstract fun call(thisValue: LAny?, args: List<LAny>): LAny

    public abstract val name: String?

    override fun getMember(name: String): LAny? {
        return null
    }

    override fun toString(): String {
        if (name != null) {
            return "<function $name>"
        }
        return "<function>"
    }
}
