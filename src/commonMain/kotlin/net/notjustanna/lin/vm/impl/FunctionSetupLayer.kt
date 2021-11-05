package net.notjustanna.lin.vm.impl

import net.notjustanna.lin.bytecode.CompiledNode
import net.notjustanna.lin.bytecode.CompiledParameter
import net.notjustanna.lin.vm.scope.DefaultMutableScope
import net.notjustanna.lin.vm.types.LAny
import net.notjustanna.lin.vm.types.LCompiledFunction

class FunctionSetupLayer(
    private val events: VMEvents,
    private val function: LCompiledFunction,
    private val thisValue: LAny? = null,
    arguments: List<LAny>
) : ExecutionLayer {
    private val body: CompiledNode
    private val scope: DefaultMutableScope
    private val paramsLeft: MutableList<CompiledParameter>
    private val argsLeft: MutableList<LAny>

    private var resolvedParamName: String? = null

    init {
        val (parametersId, _, bodyId) = function.data
        body = function.source.nodes.getOrElse(bodyId) { CompiledNode(emptyList(), emptyList()) }
        scope = DefaultMutableScope(function.rootScope)

        paramsLeft = function.source.functionParameters[parametersId].toMutableList()
        argsLeft = arguments.toMutableList()
    }

    override fun step() {
        while (paramsLeft.isNotEmpty()) {
            val parameter = paramsLeft.removeFirst()
            val value = argsLeft.removeFirstOrNull()

            val paramName = function.source.stringConst(parameter.nameConst)

            if (parameter.varargs) {
                TODO("Not yet implemented: varargs parameter")
            }

            if (value != null) {
                scope.declareVariable(paramName, true)
                scope.set(paramName, value)
                continue
            }

            if (parameter.defaultValueNodeId != -1) {
                val paramBody = function.source.nodes[parameter.defaultValueNodeId]
                resolvedParamName = paramName
                scope.declareVariable(paramName, true)
                events.pushLayer(
                    DefaultExecutionLayer(events, scope, function.source, paramBody, thisValue)
                )
                return
            }

            events.onThrow(Exceptions.mismatchedArgs())
            return
        }

        events.replaceLayer(
            DefaultExecutionLayer(events, DefaultMutableScope(scope), function.source, body, thisValue)
        )
    }

    override fun onReturn(value: LAny) {
        val paramName = resolvedParamName ?: error("resolvedParamName should not be null")
        scope.set(paramName, value)
    }

    override fun onThrow(value: LAny) {
        events.onThrow(value) // Keep cascading.
    }
}
