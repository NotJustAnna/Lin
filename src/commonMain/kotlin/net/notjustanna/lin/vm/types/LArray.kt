package net.notjustanna.lin.vm.types

data class LArray(val value: MutableList<LAny> = mutableListOf()) : LAny() {
    override fun truth(): Boolean {
        return value.isNotEmpty()
    }

    override val linType: String
        get() = "array"

    override fun getMember(name: String): LAny? {
        if (name == "__iterator") {
            return LFunction.Native {
                val iterator = value.iterator()
                LObject.of(
                    LString("__hasNext") to LFunction.Native { of(iterator.hasNext()) },
                    LString("__next") to LFunction.Native { iterator.next() },
                )
            }
        }
        return null
    }

    override fun toString(): String {
        return value.toString()
    }
}
