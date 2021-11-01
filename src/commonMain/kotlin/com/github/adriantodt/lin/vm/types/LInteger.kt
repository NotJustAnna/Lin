package com.github.adriantodt.lin.vm.types

data class LInteger(val value: Long) : LAny() {
    override fun truth(): Boolean {
        return value != 0L
    }
}

