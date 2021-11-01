package net.notjustanna.lin.vm.scope

import net.notjustanna.lin.vm.types.LAny

sealed class MutableScope : Scope() {
    abstract fun declareVariable(name: String, mutable: Boolean)

    protected abstract fun implSet(name: String, value: LAny)

    override fun set(name: String, value: LAny) {
        var s: Scope? = this
        while (s != null) {
            if (s !is MutableScope) {
                s = s.parent
                continue
            }
            val mutable = s.implIsDeclared(name)
            if (mutable == null) {
                s = s.parent
                continue
            }
            if (!mutable && s.implIsSet(name)) {
                throw IllegalStateException("Tried to set immutable variable $name with value already set.")
            }
            s.implSet(name, value)
            return
        }
        throw IllegalStateException("Tried to set variable $name not declared in any scope yet.")
    }
}
