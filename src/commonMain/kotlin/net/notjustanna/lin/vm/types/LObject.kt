package net.notjustanna.lin.vm.types

public data class LObject(val value: MutableMap<LAny, LAny> = mutableMapOf()) : LAny() {

    public constructor(vararg pairs: Pair<LAny, LAny>) : this(pairs.toMap(mutableMapOf()))

    override fun truth(): Boolean {
        return value.isNotEmpty()
    }

    override val linType: String
        get() = "object"

    override fun getMember(name: String): LAny? {
        return value[LString(name)]
    }

    override fun toString(): String {
        return value.toString()
    }

    public companion object {
        public fun of(vararg pairs: Pair<LAny, LAny>): LObject {
            return LObject(pairs.toMap(mutableMapOf()))
        }
    }
}
