package net.notjustanna.lin.vm

import net.notjustanna.lin.bytecode.CompiledSource
import net.notjustanna.lin.vm.impl.DefaultExecutionLayer
import net.notjustanna.lin.vm.impl.ExecutionLayer
import net.notjustanna.lin.vm.impl.VMEvents
import net.notjustanna.lin.vm.scope.DefaultMutableScope
import net.notjustanna.lin.vm.scope.Scope
import net.notjustanna.lin.vm.types.LAny

@Suppress("MemberVisibilityCanBePrivate", "unused")
class LinVirtualMachine(scope: Scope, source: CompiledSource) {
    private val layerStack = mutableListOf<ExecutionLayer>()
    private var currentLayer: ExecutionLayer = DefaultExecutionLayer(Events(), DefaultMutableScope(scope), source)
    private var result: VMResult? = null

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

    private inner class Events : VMEvents {
        override fun pushLayer(layer: ExecutionLayer) {
            layerStack += currentLayer
            currentLayer = layer
        }

        override fun replaceLayer(layer: ExecutionLayer) {
            currentLayer = layer
        }

        override fun onReturn(value: LAny) {
            val layer = layerStack.removeLastOrNull()
            if (layer == null) {
                result = VMResult.Returned(value)
                return
            }
            currentLayer = layer
            layer.onReturn(value)
        }

        override fun onThrow(value: LAny) {
            val layer = layerStack.removeLastOrNull()
            if (layer == null) {
                result = VMResult.Thrown(value)
                return
            }
            currentLayer = layer
            layer.onThrow(value)
        }
    }
}