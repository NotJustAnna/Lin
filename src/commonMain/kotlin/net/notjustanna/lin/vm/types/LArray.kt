package net.notjustanna.lin.vm.types

import net.notjustanna.lin.vm.LinRuntime

public data class LArray(val value: MutableList<LAny> = mutableListOf()) : LAny() {
    override fun truth(): Boolean {
        return value.isNotEmpty()
    }

    override val linType: String
        get() = "array"

    override fun getMember(name: String): LAny? {
        if (name == "__iterator") {
            return LinRuntime.iterator
        }
        return null
    }

    override fun toString(): String {
        return value.toString()
    }
}
