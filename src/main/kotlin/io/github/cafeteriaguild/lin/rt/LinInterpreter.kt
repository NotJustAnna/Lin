package io.github.cafeteriaguild.lin.rt

import io.github.cafeteriaguild.lin.ast.node.Node
import io.github.cafeteriaguild.lin.ast.node.NodeParamVisitor
import io.github.cafeteriaguild.lin.ast.node.access.*
import io.github.cafeteriaguild.lin.ast.node.declarations.*
import io.github.cafeteriaguild.lin.ast.node.invoke.InvokeExpr
import io.github.cafeteriaguild.lin.ast.node.invoke.InvokeLocalExpr
import io.github.cafeteriaguild.lin.ast.node.invoke.InvokeMemberExpr
import io.github.cafeteriaguild.lin.ast.node.misc.*
import io.github.cafeteriaguild.lin.ast.node.nodes.*
import io.github.cafeteriaguild.lin.ast.node.ops.*
import io.github.cafeteriaguild.lin.rt.exc.*
import io.github.cafeteriaguild.lin.rt.lib.*
import io.github.cafeteriaguild.lin.rt.scope.BasicScope
import io.github.cafeteriaguild.lin.rt.scope.LocalProperty
import io.github.cafeteriaguild.lin.rt.scope.Scope

class LinInterpreter : NodeParamVisitor<Scope, LObj> {
    fun execute(node: Node, scope: Scope = BasicScope()): LObj {
        if (node.accept(NodeValidator)) {
            return try {
                node.accept(this, scope)
            } catch (r: ReturnException) {
                r.value
            } catch (b: BreakException) {
                throw LinException("Unbound break signal", b)
            } catch (c: ContinueException) {
                throw LinException("Unbound continue signal", c)
            }
        }
        throw LinException("Node is invalid or contains invalid children nodes")
    }

    override fun visit(node: NullExpr, param: Scope) = LNull

    override fun visit(node: IntExpr, param: Scope) = LInt(node.value)

    override fun visit(node: LongExpr, param: Scope) = LLong(node.value)

    override fun visit(node: FloatExpr, param: Scope) = LFloat(node.value)

    override fun visit(node: DoubleExpr, param: Scope) = LDouble(node.value)

    override fun visit(node: BooleanExpr, param: Scope) = LBoolean.valueOf(node.value)

    override fun visit(node: AssignNode, param: Scope) = block {
        val property = param.findProperty(node.name) ?: throw LinException("${node.name} does not exist.")
        if (!property.setAllowed) throw LinException("Property ${node.name} does not allow assignments.")
        val value = node.value.accept(this, param)
        property.set(value)
    }

    override fun visit(node: IdentifierExpr, param: Scope): LObj {
        val property = param.findProperty(node.name) ?: throw LinException("${node.name} does not exist.")
        if (!property.getAllowed) throw LinException("Property ${node.name} does not allow access.")
        return property.get()
    }

    override fun visit(node: DeclareClassNode, param: Scope) = block {
        TODO("Not yet implemented")
    }

    override fun visit(node: DeclareEnumClassNode, param: Scope) = block {
        TODO("Not yet implemented")
    }

    override fun visit(node: DeclareInterfaceNode, param: Scope) = block {
        TODO("Not yet implemented")
    }

    override fun visit(node: DeclareObjectNode, param: Scope) = block {
        TODO("Not yet implemented")
    }

    override fun visit(node: DeclareFunctionNode, param: Scope) = block {
        TODO("Not yet implemented")
    }

    override fun visit(node: DeclareVariableNode, param: Scope) = block {
        val p = LocalProperty(node.mutable)
        param.declareProperty(node.name, p)
        node.value?.let {
            if (p.setAllowed) {
                val value = it.accept(this, param)
                p.set(value)
            }
        }
    }

    override fun visit(node: DelegatingVariableNode, param: Scope) = block {
    }

    override fun visit(node: DestructuringVariableNode, param: Scope) = block {
        val properties = node.names
            .map { it to LocalProperty(node.mutable) }
            .onEach { (s, p) -> param.declareProperty(s, p) }
        val value = node.value.accept(this, param)
        for (index in properties.indices) {
            val obj = value.component(index) ?: throw LinException("$value.component$index does not exist.")
            properties[index].second.set(obj)
        }
    }

    override fun visit(node: ReturnExpr, param: Scope): LObj {
        throw ReturnException(node.value.accept(this, param))
    }

    override fun visit(node: ThrowExpr, param: Scope): LObj {
        throw ThrowException(node.value.accept(this, param))
    }

    override fun visit(node: CharExpr, param: Scope) = LChar(node.value)

    override fun visit(node: StringExpr, param: Scope) = LString(node.value)

    override fun visit(node: UnitExpr, param: Scope) = LUnit

    override fun visit(node: MultiNode, param: Scope): LObj = block {
        for (child in node.list) {
            try {
                child.accept(this, param)
            } catch (r: ReturnException) {
                return r.value
            }
        }
    }

    override fun visit(node: MultiExpr, param: Scope): LObj {
        for (child in node.list) {
            try {
                child.accept(this, param)
            } catch (r: ReturnException) {
                return r.value
            }
        }
        return try {
            node.last.accept(this, param)
        } catch (r: ReturnException) {
            r.value
        }
    }

    override fun visit(node: InvalidNode, param: Scope): LObj {
        throw LinException("Invalid Node reached")
    }

    override fun visit(node: PropertyAccessExpr, param: Scope): LObj {
        val target = node.target.accept(this, param)
        if (node.nullSafe && target == LNull) return LNull
        val property = target.property(node.name) ?: throw LinException("$target.${node.name} does not exist.")
        if (!property.getAllowed) throw LinException("Property $target.${node.name} does not allow access.")
        return property.get()
    }

    override fun visit(node: PropertyAssignNode, param: Scope) = block {
        val target = node.target.accept(this, param)
        if (node.nullSafe && target == LNull) return LUnit
        val property = target.property(node.name) ?: throw LinException("$target.${node.name} does not exist.")
        if (!property.setAllowed) throw LinException("Property $target.${node.name} does not allow assignments.")
        val value = node.value.accept(this, param)
        property.set(value)
    }

    override fun visit(node: SubscriptAccessExpr, param: Scope): LObj {
        TODO("Not yet implemented")
    }

    override fun visit(node: SubscriptAssignNode, param: Scope): LObj {
        TODO("Not yet implemented")
    }

    override fun visit(node: InvokeExpr, param: Scope): LObj {
        val target = node.target.accept(this, param)
        if (target !is LCallable) {
            throw LinException("$target is not callable")
        }
        val args = node.arguments.map { it.accept(this, param) }
        return target.invoke(args)
    }

    override fun visit(node: InvokeLocalExpr, param: Scope): LObj {
        val property = param.findProperty(node.name) ?: throw LinException("${node.name} does not exist.")
        if (!property.getAllowed) throw LinException("Property ${node.name} does not allow access.")
        val callable = property.get()
        if (callable !is LCallable) {
            throw LinException("$callable is not callable")
        }
        val args = node.arguments.map { it.accept(this, param) }
        return callable.invoke(args)
    }

    override fun visit(node: InvokeMemberExpr, param: Scope): LObj {
        val target = node.target.accept(this, param)
        if (node.nullSafe && target == LNull) return LNull
        val property = target.property(node.name) ?: throw LinException("$target.${node.name} does not exist.")
        if (!property.getAllowed) throw LinException("Property $target.${node.name} does not allow access.")
        val callable = property.get()
        if (callable !is LCallable) {
            throw LinException("$callable is not callable")
        }
        val args = node.arguments.map { it.accept(this, param) }
        return callable.invoke(args)
    }

    override fun visit(node: IfExpr, param: Scope): LObj {
        val condition = node.condition.accept(this, BasicScope(param))
        if (condition !is LBoolean) {
            throw LinTypeException("$condition is not a Boolean")
        }
        return if (condition.value) {
            node.thenBranch.accept(this, BasicScope(param))
        } else {
            node.elseBranch?.accept(this, BasicScope(param)) ?: LUnit
        }
    }

    override fun visit(node: IfNode, param: Scope): LObj {
        val condition = node.condition.accept(this, BasicScope(param))
        if (condition !is LBoolean) {
            throw LinTypeException("$condition is not a Boolean")
        }
        return (if (condition.value) node.thenBranch else node.thenBranch).accept(this, BasicScope(param))
    }

    override fun visit(node: NotNullExpr, param: Scope): LObj {
        val value = node.value.accept(this, param)
        if (value == LNull) {
            throw LinTypeException("Assertion failed (value is null)")
        }
        return value
    }

    override fun visit(node: DoWhileNode, param: Scope) = block {
        while (true) {
            try {
                node.body.accept(this, BasicScope(param))
            } catch (_: BreakException) {
                break
            } catch (_: ContinueException) {
                continue
            }
            val condition = node.condition.accept(this, BasicScope(param))
            if (condition !is LBoolean) {
                throw LinTypeException("$condition is not a Boolean")
            }
            if (!condition.value) break
        }
    }

    override fun visit(node: WhileNode, param: Scope) = block {
        while (true) {
            val condition = node.condition.accept(this, BasicScope(param))
            if (condition !is LBoolean) {
                throw LinTypeException("$condition is not a Boolean")
            }
            if (!condition.value) break
            try {
                node.body.accept(this, BasicScope(param))
            } catch (_: BreakException) {
                break
            } catch (_: ContinueException) {
                continue
            }
        }
    }

    override fun visit(node: ForNode, param: Scope): LObj {
        TODO("Not yet implemented")
    }

    override fun visit(node: BreakExpr, param: Scope) = throw BreakException()

    override fun visit(node: ContinueExpr, param: Scope) = throw ContinueException()

    override fun visit(node: BinaryOperation, param: Scope): LObj {
        val left = node.left.accept(this, param)
        when (node.operator) {
            BinaryOperationType.ADD -> {
                val right = node.right.accept(this, param)
                if (left is LNumber && right is LNumber) {
                    return left + right
                }
                if (left is LString) {
                    return left + right
                }
                if (left is LChar) {
                    return left + right
                }
                throw LinTypeException("Unsupported operation $left + $right")
            }
            BinaryOperationType.SUBTRACT -> {
                val right = node.right.accept(this, param)
                if (left is LNumber && right is LNumber) {
                    return left - right
                }
                throw LinTypeException("Unsupported operation $left - $right")
            }
            BinaryOperationType.MULTIPLY -> {
                val right = node.right.accept(this, param)
                if (left is LNumber && right is LNumber) {
                    return left * right
                }
                throw LinTypeException("Unsupported operation $left * $right")
            }
            BinaryOperationType.DIVIDE -> {
                val right = node.right.accept(this, param)
                if (left is LNumber && right is LNumber) {
                    return left / right
                }
                throw LinTypeException("Unsupported operation $left / $right")
            }
            BinaryOperationType.REMAINING -> {
                val right = node.right.accept(this, param)
                if (left is LNumber && right is LNumber) {
                    return left % right
                }
                throw LinTypeException("Unsupported operation $left % $right")
            }
            BinaryOperationType.EQUALS -> {
                val right = node.right.accept(this, param)
                return LBoolean.valueOf(left == right)
            }
            BinaryOperationType.NOT_EQUALS -> {
                val right = node.right.accept(this, param)
                return LBoolean.valueOf(left != right)
            }
            BinaryOperationType.AND -> {
                if (left is LBoolean) {
                    if (!left.value) {
                        return left
                    }
                    val right = node.right.accept(this, param)
                    if (right is LBoolean) {
                        return right
                    }
                    throw LinTypeException("Unsupported operation $left && $right")
                }
                throw LinTypeException("Unsupported operation $left && ...")
            }
            BinaryOperationType.OR -> {
                if (left is LBoolean) {
                    if (left.value) {
                        return left
                    }
                    val right = node.right.accept(this, param)
                    if (right is LBoolean) {
                        return right
                    }
                    throw LinTypeException("Unsupported operation $left || $right")
                }
                throw LinTypeException("Unsupported operation $left || ...")
            }
            BinaryOperationType.LT -> {
                val right = node.right.accept(this, param)
                if (left is LNumber && right is LNumber) {
                    return LBoolean.valueOf(left < right)
                }
                throw LinTypeException("Unsupported operation $left < $right")
            }
            BinaryOperationType.LTE -> {
                val right = node.right.accept(this, param)
                if (left is LNumber && right is LNumber) {
                    return LBoolean.valueOf(left <= right)
                }
                throw LinTypeException("Unsupported operation $left <= $right")
            }
            BinaryOperationType.GT -> {
                val right = node.right.accept(this, param)
                if (left is LNumber && right is LNumber) {
                    return LBoolean.valueOf(left > right)
                }
                throw LinTypeException("Unsupported operation $left > $right")
            }
            BinaryOperationType.GTE -> {
                val right = node.right.accept(this, param)
                if (left is LNumber && right is LNumber) {
                    return LBoolean.valueOf(left >= right)
                }
                throw LinTypeException("Unsupported operation $left >= $right")
            }
            BinaryOperationType.ELVIS -> {
                return if (left != LNull) left else node.right.accept(this, param)
            }
            BinaryOperationType.IN -> {
                val right = node.right.accept(this, param)
                throw LinTypeException("Unsupported operation $left in $right")
            }
            BinaryOperationType.RANGE -> {
                val right = node.right.accept(this, param)
                throw LinTypeException("Unsupported operation $left..$right")
            }
        }
    }

    override fun visit(node: UnaryOperation, param: Scope): LObj {
        val target = node.target.accept(this, param)
        when (node.operator) {
            UnaryOperationType.POSITIVE -> {
                if (target is LNumber) {
                    return +target
                }
                throw LinTypeException("Unsupported operation +$target")
            }
            UnaryOperationType.NEGATIVE -> {
                if (target is LNumber) {
                    return -target
                }
                throw LinTypeException("Unsupported operation -$target")
            }
            UnaryOperationType.NOT -> {
                if (target is LBoolean) {
                    return target.not()
                }
                throw LinTypeException("Unsupported operation !$target")
            }
        }
    }

    override fun visit(node: PreAssignUnaryOperation, param: Scope): LObj {
        TODO("Not yet implemented")
    }

    override fun visit(node: PostAssignUnaryOperation, param: Scope): LObj {
        TODO("Not yet implemented")
    }

    override fun visit(node: AssignOperation, param: Scope): LObj {
        TODO("Not yet implemented")
    }

    override fun visit(node: ObjectExpr, param: Scope): LObj {
        TODO("Not yet implemented")
    }

    override fun visit(node: FunctionExpr, param: Scope): LObj {
        TODO("Not yet implemented")
    }

    override fun visit(node: LambdaExpr, param: Scope): LObj {
        TODO("Not yet implemented")
    }

    override fun visit(node: InitializerNode, param: Scope): LObj {
        TODO("Not yet implemented")
    }

    private inline fun block(block: () -> Unit): LUnit {
        block()
        return LUnit
    }
}