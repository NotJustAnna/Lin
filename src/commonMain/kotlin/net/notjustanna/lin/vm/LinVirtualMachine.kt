package net.notjustanna.lin.vm

import net.notjustanna.lin.bytecode.CompiledSource
import net.notjustanna.lin.vm.impl.DefaultExecutionLayer
import net.notjustanna.lin.vm.impl.ExecutionLayer
import net.notjustanna.lin.vm.impl.VMEvents
import net.notjustanna.lin.vm.scope.DefaultMutableScope
import net.notjustanna.lin.vm.scope.Scope
import net.notjustanna.lin.vm.types.LAny

@Suppress("MemberVisibilityCanBePrivate", "unused")
public class LinVirtualMachine(layerInitializer: (VMEvents) -> ExecutionLayer) {
    public constructor(source: CompiledSource, scope: Scope) : this({
        DefaultExecutionLayer(it, DefaultMutableScope(scope), source, "<main>")
    })

    private val layerStack = mutableListOf<ExecutionLayer>()
    private var currentLayer: ExecutionLayer = layerInitializer(EventsImpl(this))
    private var result: LinResult? = null

    public fun run(): LinResult {
        while (hasNextStep()) {
            step()
        }
        return result()
    }

    public fun hasNextStep(): Boolean {
        return result == null
    }

    public fun step() {
        currentLayer.step()
    }

    public fun result(): LinResult {
        return result ?: throw RuntimeException("Execution not finished")
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
                vm.result = LinResult.Returned(value)
                return
            }
            vm.currentLayer = layer
            layer.onReturn(value)
        }

        override fun onThrow(value: LAny) {
            val layer = vm.layerStack.removeLastOrNull()
            if (layer == null) {
                vm.result = LinResult.Thrown(value)
                return
            }
            vm.currentLayer = layer
            layer.onThrow(value)
        }

        override fun stackTrace(): List<StackTrace> {
            return (vm.layerStack + vm.currentLayer).asReversed().mapNotNull(ExecutionLayer::trace)
        }
    }
}
