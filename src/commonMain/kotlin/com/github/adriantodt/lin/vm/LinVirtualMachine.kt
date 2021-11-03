package com.github.adriantodt.lin.vm

import com.github.adriantodt.lin.bytecode.CompiledSource
import com.github.adriantodt.lin.vm.impl.DefaultExecutionLayer
import com.github.adriantodt.lin.vm.impl.ExecutionLayer
import com.github.adriantodt.lin.vm.impl.VMEvents
import com.github.adriantodt.lin.vm.scope.DefaultMutableScope
import com.github.adriantodt.lin.vm.scope.Scope
import com.github.adriantodt.lin.vm.types.LAny

@Suppress("MemberVisibilityCanBePrivate", "unused")
class LinVirtualMachine(layerInitializer: (VMEvents) -> ExecutionLayer) {

    constructor(source: CompiledSource, scope: Scope) : this({
        DefaultExecutionLayer(it, DefaultMutableScope(scope), source)
    })

    private val layerStack = mutableListOf<ExecutionLayer>()
    private var currentLayer: ExecutionLayer
    private var result: VMResult? = null

    init {
        currentLayer = layerInitializer(EventsImpl(this))
    }

    fun run(): LAny {
        while (hasNextStep()) {
            step()
        }
        return result()
    }

    fun hasNextStep(): Boolean {
        return result == null
    }

    fun step() {
        currentLayer.step()
    }

    fun result(): LAny {
        when (val r = result) {
            is VMResult.Returned -> return r.value
            is VMResult.Thrown -> throw LAnyException(r.value)
            null -> throw RuntimeException("Execution not finished")
        }
    }

    private sealed class VMResult {
        class Returned(val value: LAny) : VMResult()
        class Thrown(val value: LAny) : VMResult()
    }

    private class EventsImpl(private val vm: LinVirtualMachine) : VMEvents {
        override fun pushLayer(layer: ExecutionLayer) {
            vm.layerStack += vm.currentLayer
            vm.currentLayer = layer
        }

        override fun replaceLayer(layer: ExecutionLayer) {
            vm.currentLayer = layer
        }

        override fun onReturn(value: LAny) {
            val layer = vm.layerStack.removeLastOrNull()
            if (layer == null) {
                vm.result = VMResult.Returned(value)
                return
            }
            vm.currentLayer = layer
            layer.onReturn(value)
        }

        override fun onThrow(value: LAny) {
            val layer = vm.layerStack.removeLastOrNull()
            if (layer == null) {
                vm.result = VMResult.Thrown(value)
                return
            }
            vm.currentLayer = layer
            layer.onThrow(value)
        }
    }
}
