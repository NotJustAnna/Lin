package net.notjustanna.lin.vm.types

data class LDecimal(val value: Double) : LAny() {
    override fun truth(): Boolean {
        return value != 0.0
    }
}
