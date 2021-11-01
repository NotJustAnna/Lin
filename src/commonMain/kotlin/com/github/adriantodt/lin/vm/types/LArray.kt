package com.github.adriantodt.lin.vm.types

data class LArray(val value: MutableList<LAny> = mutableListOf()) : LAny() {
    override fun truth(): Boolean {
        return value.isNotEmpty()
    }
}
