package io.github.cafeteriaguild.lin.rt.lib.lang.functions

import io.github.cafeteriaguild.lin.ast.node.Node
import io.github.cafeteriaguild.lin.ast.node.nodes.LambdaExpr
import io.github.cafeteriaguild.lin.rt.LinInterpreter
import io.github.cafeteriaguild.lin.rt.exceptions.LinException
import io.github.cafeteriaguild.lin.rt.exceptions.LinUnboundSignalException
import io.github.cafeteriaguild.lin.rt.exceptions.internal.BreakException
import io.github.cafeteriaguild.lin.rt.exceptions.internal.ContinueException
import io.github.cafeteriaguild.lin.rt.exceptions.internal.ReturnException
import io.github.cafeteriaguild.lin.rt.lib.LObj
import io.github.cafeteriaguild.lin.rt.lib.nativelang.invoke.LinDirectCall
import io.github.cafeteriaguild.lin.rt.scope.BasicScope
import io.github.cafeteriaguild.lin.rt.scope.Scope
import io.github.cafeteriaguild.lin.rt.scope.UserScope

class CompiledLambda(
    private val parentScope: Scope,
    private val parameters: List<LambdaExpr.Parameter>,
    private val body: Node
) : LObj, LinDirectCall {
    override fun canInvoke(): Boolean {
        return true
    }

    override fun invoke(args: List<LObj>): LObj {
        val interpreter = LinInterpreter()
        val params = prepareParameters(interpreter, args)
        return interpreter.execute(body, BasicScope(params))
    }

    override fun call(interpreter: LinInterpreter, args: List<LObj>): LObj {
        val params = prepareParameters(interpreter, args)
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

    private fun prepareParameters(interpreter: LinInterpreter, args: List<LObj>): UserScope {
        val params = UserScope(parentScope)
        if (parameters.isEmpty()) {
            if (args.size == 1) {
                params["it"] = args.single()
            } else if (args.size > 1) {
                throw LinException("Lambda only accepts a single parameter or no parameter.")
            }
        } else if (parameters.size == args.size) {
            for ((param, arg) in parameters.zip(args)) {
                when (param) {
                    is LambdaExpr.Parameter.Destructured -> {
                        for ((i, name) in param.names.withIndex()) {
                            val callable = arg.component(i) ?: throw LinException("$arg.component$i does not exist.")
                            params[name] = if (callable is LinDirectCall) callable.call(interpreter) else callable.invoke()
                        }
                    }
                    is LambdaExpr.Parameter.Named -> {
                        params[param.name] = arg
                    }
                }
            }
        } else {
            throw LinException(
                "Lambda accepts ${parameters.size} parameter${if (parameters.size == 1) "" else "s"}."
            )
        }
        return params
    }

    override fun toString(): String {
        return "lambda { | ${parameters.joinToString(", ")} | -> ... }"
    }
}
