package com.github.adriantodt.lin.vm.types

object LNull : LAny() {
    override fun truth(): Boolean {
        return false
    }
}

