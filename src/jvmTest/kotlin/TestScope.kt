import com.github.adriantodt.lin.vm.scope.ImmutableMapScope
import com.github.adriantodt.lin.vm.scope.Scope
import com.github.adriantodt.lin.vm.types.LAny
import com.github.adriantodt.lin.vm.types.LFunction
import com.github.adriantodt.lin.vm.types.LNull

fun testScope(): Scope {
    val map = mapOf<String, LAny>(
        "println" to LFunction.Native {
            println(it.joinToString(" "))

            LNull
        }
    )
    return ImmutableMapScope(map, null)
}
