package net.notjustanna.lin.vm.types

public object LNull : LAny() {
    override fun truth(): Boolean {
        return false
    }

    override val linType: String
        get() = "null"

    override fun getMember(name: String): LAny? {
        return null
    }

    override fun toString(): String {
        return "null"
    }
}

