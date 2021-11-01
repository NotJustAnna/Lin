package net.notjustanna.lin.vm.types

object LTrue : LAny() {
    override fun truth(): Boolean {
        return true
    }
}
