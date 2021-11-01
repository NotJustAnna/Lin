package com.github.adriantodt.lin.vm.types

object LTrue : LAny() {
    override fun truth(): Boolean {
        return true
    }
}
