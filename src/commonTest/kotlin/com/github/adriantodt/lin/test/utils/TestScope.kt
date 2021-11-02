package com.github.adriantodt.lin.test.utils

import com.github.adriantodt.lin.vm.scope.ImmutableMapScope
import com.github.adriantodt.lin.vm.types.LAny
import com.github.adriantodt.lin.vm.types.LFunction
import com.github.adriantodt.lin.vm.types.LNull

class TestScope {
    val input = mutableListOf<LAny>()
    val output = mutableListOf<LAny>()

    val scope = ImmutableMapScope(mapOf(
        "retrieve" to LFunction.Native { input.removeFirst() },
        "publish" to LFunction.Native { output.addAll(it); LNull }
    ), null)
}
