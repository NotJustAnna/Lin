package net.notjustanna.lin.vm.scope

import net.notjustanna.lin.vm.types.LAny

public class ImmutableMapScope(private val map: Map<String, LAny>, override val parent: Scope?) : Scope() {
    override fun implIsDeclared(name: String): Boolean? {
        if (name in map) {
            return false
        }
        return null
    }

    override fun implIsSet(name: String): Boolean {
        return name in map
    }

    override fun implGet(name: String): LAny {
        return map.getValue(name)
    }
}
