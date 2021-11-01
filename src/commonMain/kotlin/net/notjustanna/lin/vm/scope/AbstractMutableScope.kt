package net.notjustanna.lin.vm.scope

sealed class AbstractMutableScope : MutableScope() {
    private val declared = mutableMapOf<String, Boolean>()

    override fun declareVariable(name: String, mutable: Boolean) {
        if (declared.contains(name)) {
            throw IllegalStateException("Variable $name already exists.")
        }
        declared[name] = mutable
    }

    override fun implIsDeclared(name: String): Boolean? {
        return declared[name]
    }
}
