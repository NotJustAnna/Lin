package net.notjustanna.lin.vm.types

object LFalse : LAny() {
    override fun truth(): Boolean {
        return false
    }
}
