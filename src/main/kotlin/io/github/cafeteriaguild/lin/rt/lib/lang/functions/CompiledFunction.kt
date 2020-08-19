package io.github.cafeteriaguild.lin.rt.lib.lang.functions

import io.github.cafeteriaguild.lin.ast.node.Node
import io.github.cafeteriaguild.lin.ast.node.nodes.FunctionExpr
import io.github.cafeteriaguild.lin.rt.LinInterpreter
import io.github.cafeteriaguild.lin.rt.NodeValidator
import io.github.cafeteriaguild.lin.rt.exceptions.LinException
import io.github.cafeteriaguild.lin.rt.exceptions.LinUnboundSignalException
import io.github.cafeteriaguild.lin.rt.exceptions.internal.BreakException
import io.github.cafeteriaguild.lin.rt.exceptions.internal.ContinueException
import io.github.cafeteriaguild.lin.rt.exceptions.internal.ReturnException
import io.github.cafeteriaguild.lin.rt.lib.LObj
import io.github.cafeteriaguild.lin.rt.lib.lang.collections.LList
import io.github.cafeteriaguild.lin.rt.lib.nativelang.invoke.LinDirectCall
import io.github.cafeteriaguild.lin.rt.scope.BasicScope
import io.github.cafeteriaguild.lin.rt.scope.Scope
import io.github.cafeteriaguild.lin.rt.scope.UserScope

class CompiledFunction(
    private val parentScope: Scope,
    parameters: List<FunctionExpr.Parameter>,
    private val body: Node?
) : LObj, LinDirectCall {
    private val paramsBeforeVarargs: List<FunctionExpr.Parameter>
    private val varargsParam: FunctionExpr.Parameter?
    private val paramsAfterVarargs: List<FunctionExpr.Parameter>

    init {
        if (body?.accept(NodeValidator) == false) {
            throw LinException("Body is invalid or contains invalid children nodes")
        }
        if (parameters.count { it.varargs } > 1) {
            throw LinException("Unsupported multiple varargs in the same function")
        }
        if (!parameters.asSequence().mapNotNull { it.value }.all { it.accept(NodeValidator) }) {
            throw LinException("Parameters contains invalid value nodes")
        }
        val indexOf = parameters.indexOfFirst { it.varargs }
        if (indexOf != -1) {
            paramsBeforeVarargs = parameters.subList(0, indexOf)
            varargsParam = parameters[indexOf]
            paramsAfterVarargs = parameters.subList(indexOf + 1, parameters.size)
        } else {
            paramsBeforeVarargs = parameters
            varargsParam = null
            paramsAfterVarargs = emptyList()
        }
    }

    override fun canInvoke(): Boolean {
        return true
    }

    override fun invoke(args: List<LObj>): LObj {
        if (body == null) {
            throw LinException("This function has no body.")
        }
        val interpreter = LinInterpreter()
        val params = prepareParameters(interpreter, args.toMutableList())
        return interpreter.execute(body, BasicScope(params))
    }

    override fun call(interpreter: LinInterpreter, args: List<LObj>): LObj {
        if (body == null) {
            throw LinException("This function has no body.")
        }
        val params = prepareParameters(interpreter, args.toMutableList())
        return try {
            body.accept(interpreter, params)
        } catch (r: ReturnException) {
            r.value
        } catch (b: BreakException) {
            throw LinUnboundSignalException("Unbound break signal", b)
        } catch (c: ContinueException) {
            throw LinUnboundSignalException("Unbound continue signal", c)
        }
    }

    @OptIn(ExperimentalStdlibApi::class)
    private fun prepareParameters(interpreter: LinInterpreter, arguments: MutableList<LObj>): UserScope {
        val params = UserScope(parentScope)
        val argsLeft = arguments.toMutableList()

        for (param in paramsBeforeVarargs) {
            params[param.name] = argsLeft.removeFirstOrNull()
                ?: param.value?.let { interpreter.execute(it, params) }
                    ?: throw LinException(paramString())
        }
        for (param in paramsAfterVarargs.asReversed()) {
            params[param.name] = argsLeft.removeLastOrNull()
                ?: param.value?.let { interpreter.execute(it, params) }
                    ?: throw LinException(paramString())
        }
        if (varargsParam != null) {
            params[varargsParam.name] = LList(argsLeft).takeIf { argsLeft.isNotEmpty() }
                ?: varargsParam.value?.let { interpreter.execute(it, params) }
                    ?: LList(emptyList())
        }

        return params
    }

    private fun paramString(): String {
        val i = paramsBeforeVarargs.size + paramsAfterVarargs.size
        val plural = if (i == 1) "" else "s"
        val maybeAtLeast = if (varargsParam != null) "at least " else ""
        return "Function requires $maybeAtLeast$i parameter$plural."
    }

    override fun toString(): String {
        return buildString {
            append("fun(")
            append(
                listOf(
                    paramsBeforeVarargs.map { if (it.value != null) it.name else "${it.name} = ..." },
                    listOfNotNull(varargsParam).map { "vararg ${if (it.value != null) it.name else "${it.name} = ..."}" },
                    paramsAfterVarargs.map { if (it.value != null) it.name else "${it.name} = ..." }
                ).flatten().joinToString(", ")
            )
            if (body != null) {
                append(" { ... }")
            }
        }
    }
}
