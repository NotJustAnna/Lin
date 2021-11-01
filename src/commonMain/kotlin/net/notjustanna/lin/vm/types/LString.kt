package net.notjustanna.lin.vm.types

data class LString(val value: String) : LAny() {
    override fun truth(): Boolean {
        return value.isNotEmpty()
    }
}
