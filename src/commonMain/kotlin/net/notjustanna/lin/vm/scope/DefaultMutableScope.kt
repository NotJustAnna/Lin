package net.notjustanna.lin.vm.scope

import net.notjustanna.lin.vm.types.LAny

public class DefaultMutableScope(override val parent: Scope?) : AbstractMutableScope() {
    private val map = mutableMapOf<String, LAny>()

    override fun implSet(name: String, value: LAny) {
        map[name] = value
    }

    override fun implIsSet(name: String): Boolean {
        return name in map
    }

    override fun implGet(name: String): LAny {
        return map.getValue(name)
    }

    public fun immutable(): ImmutableMapScope {
        return ImmutableMapScope(map.toMap(), parent)
    }
}
