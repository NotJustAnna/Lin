package net.notjustanna.lin.vm.types

data class LArray(val value: MutableList<LAny> = mutableListOf()) : LAny() {
    override fun truth(): Boolean {
        return value.isNotEmpty()
    }
}
