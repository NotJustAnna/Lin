package com.github.adriantodt.lin.vm.types

import com.github.adriantodt.lin.vm.LinRuntime

class LRange(val value: LongRange) : LAny() {
    override fun truth(): Boolean {
        return true
    }

    override val linType: String
        get() = "range"

    override fun getMember(name: String): LAny? {
        if (name == "__iterator") {
            return LinRuntime.iterator
        }
        return null
    }
}
