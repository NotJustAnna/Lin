package com.github.adriantodt.lin.vm.types

data class LString(val value: String) : LAny() {
    override fun truth(): Boolean {
        return value.isNotEmpty()
    }
}
