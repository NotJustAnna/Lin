package com.github.adriantodt.lin.vm.impl

import com.github.adriantodt.lin.bytecode.CompiledNode
import com.github.adriantodt.lin.bytecode.CompiledParameter
import com.github.adriantodt.lin.vm.scope.DefaultMutableScope
import com.github.adriantodt.lin.vm.types.LAny
import com.github.adriantodt.lin.vm.types.LFunction

class FunctionSetupLayer(
    private val events: VMEvents,
    private val function: LFunction.Compiled,
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

    private fun processArguments() {
        while (paramsLeft.isNotEmpty()) {
            val parameter = paramsLeft.removeFirst()
            val value = argsLeft.removeFirstOrNull()

            val paramName = function.source.stringPool[parameter.nameConst]

            // TODO Implement Varargs
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

            TODO("Properly send 'mismatched_args' error downstream.")
        }

        events.replaceLayer(
            DefaultExecutionLayer(events, DefaultMutableScope(scope), function.source, body, thisValue)
        )
    }

    override fun step() {
        processArguments()
    }

    override fun onReturn(value: LAny) {
        val paramName = resolvedParamName ?: error("resolvedParamName should not be null")
        scope.set(paramName, value)
    }

    override fun onThrow(value: LAny) {
        events.onThrow(value) // Keep cascading.
    }
}
