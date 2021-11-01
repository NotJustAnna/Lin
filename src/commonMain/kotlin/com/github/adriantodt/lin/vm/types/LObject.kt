package com.github.adriantodt.lin.vm.types

data class LObject(val value: MutableMap<LAny, LAny>) : LAny() {
    override fun truth(): Boolean {
        return value.isNotEmpty()
    }
}
