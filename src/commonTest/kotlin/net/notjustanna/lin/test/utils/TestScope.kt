package net.notjustanna.lin.test.utils

import net.notjustanna.lin.vm.scope.ImmutableMapScope
import net.notjustanna.lin.vm.types.LAny
import net.notjustanna.lin.vm.types.LFunction
import net.notjustanna.lin.vm.types.LNull

class TestScope {
    val input = mutableListOf<LAny>()
    val output = mutableListOf<LAny>()

    val scope = ImmutableMapScope(mapOf(
        "retrieve" to LFunction.Native { input.removeFirst() },
        "publish" to LFunction.Native { output.addAll(it); LNull }
    ), null)
}
