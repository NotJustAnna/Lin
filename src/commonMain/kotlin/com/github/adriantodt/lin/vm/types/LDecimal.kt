package com.github.adriantodt.lin.vm.types

data class LDecimal(val value: Double) : LAny() {
    override fun truth(): Boolean {
        return value != 0.0
    }
}
