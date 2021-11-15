package net.notjustanna.lin.vm.types

public class LNativeFunction(
    override val name: String? = null,
    public val block: (thisValue: LAny?, args: List<LAny>) -> LAny
) : LFunction() {
    override fun invoke(vararg args: LAny): LAny {
        return block(null, args.toList())
    }

    override fun call(thisValue: LAny?, args: List<LAny>): LAny {
        return block(thisValue, args)
    }
}
