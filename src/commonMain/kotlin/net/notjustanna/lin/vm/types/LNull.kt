package net.notjustanna.lin.vm.types

object LNull : LAny() {
    override fun truth(): Boolean {
        return false
    }
}

