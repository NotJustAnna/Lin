import net.notjustanna.lin.vm.scope.ImmutableMapScope
import net.notjustanna.lin.vm.scope.Scope
import net.notjustanna.lin.vm.types.LAny
import net.notjustanna.lin.vm.types.LFunction
import net.notjustanna.lin.vm.types.LNull

fun testScope(): Scope {
    val map = mapOf<String, LAny>(
        "println" to LFunction.Native {
            println(it.joinToString(" "))

            LNull
        }
    )
    return ImmutableMapScope(map, null)
}
