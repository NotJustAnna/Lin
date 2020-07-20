package io.github.cafeteriaguild.lin.rt.lib

import io.github.cafeteriaguild.lin.ast.node.Node
import io.github.cafeteriaguild.lin.ast.node.nodes.LambdaExpr
import io.github.cafeteriaguild.lin.rt.LinInterpreter
import io.github.cafeteriaguild.lin.rt.exc.LinException
import io.github.cafeteriaguild.lin.rt.scope.BasicScope
import io.github.cafeteriaguild.lin.rt.scope.Scope
import io.github.cafeteriaguild.lin.rt.scope.UserScope

class LLambda(
    private val parentScope: Scope,
    private val lambdaParams: List<LambdaExpr.Parameter>,
    private val body: Node
) : LCallable {
    override fun invoke(parameters: List<LObj>): LObj {
        val params = UserScope(parentScope)

        if (lambdaParams.isEmpty()) {
            if (parameters.size == 1) {
                params["it"] = parameters.single()
            } else if (parameters.size > 1) {
                throw LinException("Lambda only accepts a single parameter or no parameter.")
            }
        } else if (lambdaParams.size == parameters.size) {
            for ((param, arg) in lambdaParams.zip(parameters)) {
                when (param) {
                    is LambdaExpr.Parameter.Destructured -> {
                        for ((i, name) in param.names.withIndex()) {
                            params[name] = arg.component(i) ?: throw LinException("$arg.component$i does not exist.")
                        }
                    }
                    is LambdaExpr.Parameter.Named -> {
                        params[param.name] = arg
                    }
                }
            }
        } else {
            throw LinException(
                "Lambda accepts ${lambdaParams.size} parameter${if (lambdaParams.size == 1) "" else "s"}."
            )
        }

        return LinInterpreter().execute(body, BasicScope(params))
    }
}
