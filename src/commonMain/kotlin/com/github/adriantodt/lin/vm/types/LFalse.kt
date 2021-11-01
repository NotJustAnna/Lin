package com.github.adriantodt.lin.vm.types

object LFalse : LAny() {
    override fun truth(): Boolean {
        return false
    }
}
