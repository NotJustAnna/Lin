package com.github.adriantodt.lin.vm.types

import com.github.adriantodt.lin.vm.LinRuntime

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
