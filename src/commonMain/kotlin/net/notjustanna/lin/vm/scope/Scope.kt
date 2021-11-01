package net.notjustanna.lin.vm.scope

import net.notjustanna.lin.vm.types.LAny

sealed class Scope {
    abstract val parent: Scope?

    /**
     * Return true if the variable is declared and mutable, false if declared and immutable, null if not declared.
     */
    protected abstract fun implIsDeclared(name: String): Boolean?

    /**
     * Return true if the variable is declared AND set.
     */
    protected abstract fun implIsSet(name: String): Boolean

    protected abstract fun implGet(name: String): LAny

    fun get(name: String): LAny {
        var s: Scope? = this
        while (s != null) {
            if (s.implIsDeclared(name) == null) {
                s = s.parent
                continue
            }
            if (!s.implIsSet(name)) {
                throw IllegalStateException("Variable $name is declared but not set.")
            }
            return s.implGet(name)
        }
        throw IllegalStateException("Could not resolve $name")
    }

    open fun set(name: String, value: LAny) {
        var s: Scope? = this
        while (s != null) {
            if (s is MutableScope) {
                s = s.parent
                continue
            }
            s.set(name, value)
            return
        }
        throw IllegalStateException("Could not find a mutable scope.")
    }
}
