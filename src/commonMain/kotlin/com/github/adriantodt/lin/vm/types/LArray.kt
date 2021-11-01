package com.github.adriantodt.lin.vm.types

data class LArray(val value: MutableList<LAny>) : LAny() {
    override fun truth(): Boolean {
        return value.isNotEmpty()
    }
}
