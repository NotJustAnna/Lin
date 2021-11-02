package com.github.adriantodt.lin.vm.types

data class LObject(val value: MutableMap<LAny, LAny> = mutableMapOf()) : LAny() {
    override fun truth(): Boolean {
        return value.isNotEmpty()
    }

    override val linType: String
        get() = "object"

    override fun getMember(name: String): LAny? {
        return value[LString(name)]
    }

    override fun toString(): String {
        return value.toString()
    }

    companion object {
        fun of(vararg pairs: Pair<LAny, LAny>): LObject {
            return LObject(pairs.toMap(mutableMapOf()))
        }
    }
}
