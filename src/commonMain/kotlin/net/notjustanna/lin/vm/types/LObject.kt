package net.notjustanna.lin.vm.types

data class LObject(val value: MutableMap<LAny, LAny>) : LAny() {
    override fun truth(): Boolean {
        return value.isNotEmpty()
    }
}
