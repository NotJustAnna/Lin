package io.github.cafeteriaguild.lin.rt.lib.lang.functions

import io.github.cafeteriaguild.lin.ast.node.Node
import io.github.cafeteriaguild.lin.ast.node.nodes.FunctionExpr
import io.github.cafeteriaguild.lin.rt.LinInterpreter
import io.github.cafeteriaguild.lin.rt.exceptions.LinException
import io.github.cafeteriaguild.lin.rt.lib.LObj
import io.github.cafeteriaguild.lin.rt.lib.lang.collections.LList
import io.github.cafeteriaguild.lin.rt.scope.BasicScope
import io.github.cafeteriaguild.lin.rt.scope.Scope
import io.github.cafeteriaguild.lin.rt.scope.UserScope
import java.util.*

class LFunction(
    private val parentScope: Scope,
    parameters: List<FunctionExpr.Parameter>,
    private val body: Node?
) : LObj {
    private val paramsBeforeVarargs: List<FunctionExpr.Parameter>
    private val varargsParam: FunctionExpr.Parameter?
    private val paramsAfterVarargs: List<FunctionExpr.Parameter>

    init {
        if (parameters.count { it.varargs } > 1) {
            throw LinException("Unsupported multiple varargs in the same function")
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

    @OptIn(ExperimentalStdlibApi::class)
    override fun invoke(args: List<LObj>): LObj {
        val interpreter = LinInterpreter()

        val params = UserScope(parentScope)
        val argsLeft = args.toMutableList()

        if (body == null) {
            throw LinException("This function has no body.")
        }

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

        return interpreter.execute(body, BasicScope(params))
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
            val j = StringJoiner(", ")
            for (p in paramsBeforeVarargs) {
                j.add(buildString {
                    append(p.name)
                    if (p.value != null) append(" = ...")
                })
            }
            if (varargsParam != null) {
                j.add(buildString {
                    append("vararg ")
                    append(varargsParam.name)
                    if (varargsParam.value != null) append(" = ...")
                })
            }
            for (p in paramsAfterVarargs) {
                j.add(buildString {
                    append(p.name)
                    if (p.value != null) append(" = ...")
                })
            }
            append(j)
            append(")")
            if (body != null) {
                append(" { ... }")
            }
        }
    }
}
