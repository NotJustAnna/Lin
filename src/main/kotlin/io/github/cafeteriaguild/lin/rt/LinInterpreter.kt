@file:Suppress("NOTHING_TO_INLINE")

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
import io.github.cafeteriaguild.lin.rt.exceptions.*
import io.github.cafeteriaguild.lin.rt.exceptions.internal.BreakException
import io.github.cafeteriaguild.lin.rt.exceptions.internal.ContinueException
import io.github.cafeteriaguild.lin.rt.exceptions.internal.ReturnException
import io.github.cafeteriaguild.lin.rt.lib.LObj
import io.github.cafeteriaguild.lin.rt.lib.lang.*
import io.github.cafeteriaguild.lin.rt.lib.lang.functions.CompiledFunction
import io.github.cafeteriaguild.lin.rt.lib.lang.functions.CompiledLambda
import io.github.cafeteriaguild.lin.rt.lib.lang.number.*
import io.github.cafeteriaguild.lin.rt.lib.lang.number.LNumber.Companion.box
import io.github.cafeteriaguild.lin.rt.lib.lang.number.LNumber.Companion.dec
import io.github.cafeteriaguild.lin.rt.lib.lang.number.LNumber.Companion.div
import io.github.cafeteriaguild.lin.rt.lib.lang.number.LNumber.Companion.inc
import io.github.cafeteriaguild.lin.rt.lib.lang.number.LNumber.Companion.minus
import io.github.cafeteriaguild.lin.rt.lib.lang.number.LNumber.Companion.plus
import io.github.cafeteriaguild.lin.rt.lib.lang.number.LNumber.Companion.rem
import io.github.cafeteriaguild.lin.rt.lib.lang.number.LNumber.Companion.times
import io.github.cafeteriaguild.lin.rt.lib.lang.number.LNumber.Companion.unaryMinus
import io.github.cafeteriaguild.lin.rt.lib.lang.number.LNumber.Companion.unaryPlus
import io.github.cafeteriaguild.lin.rt.lib.nativelang.invoke.LinCall
import io.github.cafeteriaguild.lin.rt.lib.nativelang.invoke.LinDirectCall
import io.github.cafeteriaguild.lin.rt.lib.nativelang.properties.DelegatedProperty
import io.github.cafeteriaguild.lin.rt.lib.nativelang.properties.Property
import io.github.cafeteriaguild.lin.rt.lib.nativelang.properties.SimpleProperty
import io.github.cafeteriaguild.lin.rt.lib.nativelang.properties.SubscriptEmulatedProperty
import io.github.cafeteriaguild.lin.rt.lib.nativelang.routes.*
import io.github.cafeteriaguild.lin.rt.scope.BasicScope
import io.github.cafeteriaguild.lin.rt.scope.Scope
import io.github.cafeteriaguild.lin.rt.scope.UserScope

class LinInterpreter : NodeParamVisitor<Scope, LObj>, AccessResolver<Scope, Property?> {
    fun execute(node: Node, scope: Scope = BasicScope()): LObj {
        if (node.accept(NodeValidator)) {
            return try {
                node.accept(this, scope)
            } catch (r: ReturnException) {
                r.value
            } catch (b: BreakException) {
                throw LinUnboundSignalException("Unbound break signal", b)
            } catch (c: ContinueException) {
                throw LinUnboundSignalException("Unbound continue signal", c)
            }
        }
        throw LinInvalidNodeException("Node is invalid or contains invalid children nodes")
    }

    //region primitive nodes Null, Int, Long, Float, Double, Boolean, Char, String, Unit

    override fun visit(node: NullExpr, param: Scope) = LNull

    override fun visit(node: IntExpr, param: Scope) = LInt(node.value)

    override fun visit(node: LongExpr, param: Scope) = LLong(node.value)

    override fun visit(node: FloatExpr, param: Scope) = LFloat(node.value)

    override fun visit(node: DoubleExpr, param: Scope) = LDouble(node.value)

    override fun visit(node: BooleanExpr, param: Scope) = LBoolean.of(node.value)

    override fun visit(node: CharExpr, param: Scope) = LChar(node.value)

    override fun visit(node: StringExpr, param: Scope) = LString(node.value)

    override fun visit(node: UnitExpr, param: Scope) = LUnit

    override fun visit(node: InvalidNode, param: Scope): LObj {
        throw LinInvalidNodeException("Interpreter reached a invalid node")
    }

    //endregion

    //region property access and assignment nodes

    override fun visit(node: IdentifierExpr, param: Scope): LObj {
        return resolve(node, param).getAllowedOrThrow(node.name).get()
    }

    override fun resolve(node: IdentifierExpr, param: Scope): Property {
        return param.findProperty(node.name).orNullPointer(node.name)
    }

    override fun visit(node: ThisExpr, param: Scope): LObj {
        return resolve(node, param).getAllowedOrThrow("this").get()
    }

    override fun resolve(node: ThisExpr, param: Scope): Property {
        return param.findProperty("this").orNullPointer("this")
    }

    override fun visit(node: AssignNode, param: Scope) = block {
        resolve(IdentifierExpr(node.name, node.section), param)
            .setAllowedOrThrow(node.name)
            .set(node.value.accept(this, param))
    }

    override fun visit(node: PropertyAccessExpr, param: Scope): LObj {
        val target = node.target.accept(this, param)
        if (node.nullSafe && target == LNull) return LNull
        val property = target.propertyOf(node.name)
            .orNullPointer(target, node.name)
            .getAllowedOrThrow(target, node.name)
        return property.get()
    }

    override fun resolve(node: PropertyAccessExpr, param: Scope): Property? {
        val target = node.target.accept(this, param)
        if (node.nullSafe && target == LNull) return null
        val property = target.propertyOf(node.name)
        if (property == null) {
            if (node.nullSafe) return null
            throw LinNullPointerException("'$target.${node.name}' does not exist.")
        }
        return property
    }

    override fun visit(node: PropertyAssignNode, param: Scope) = block {
        val target = node.target.accept(this, param)
        if (node.nullSafe && target == LNull) return LUnit
        target.propertyOf(node.name)
            .orNullPointer(target, node.name)
            .setAllowedOrThrow(target, node.name)
            .set(node.value.accept(this, param))
    }

    override fun resolve(node: SubscriptAccessExpr, param: Scope): Property {
        val target = node.target.accept(this, param)
        if (target is LinNativeGet || target is LinNativeSet
            || (target.canGet("get") && target["get"].canInvoke())
            || (target.canGet("set") && target["set"].canInvoke())) {
            val args = node.arguments.map { it.accept(this, param) }
            return SubscriptEmulatedProperty(target, args, this)
        }
        throw LinUnsupportedOperationException("'$target' does not accept subscript operations")
    }

    //endregion

    //region declaration nodes

    override fun visit(node: DeclareVariableNode, param: Scope) = block {
        val p = SimpleProperty(node.mutable)
        param.declareProperty(node.name, p)
        node.value?.let {
            val value = it.accept(this, param)
            p.set(value)
        }
    }

    override fun visit(node: DelegatingVariableNode, param: Scope) = block {
        val target = node.delegate.accept(this, param)
        if (target is LinNativeDelegate || target.canGet("getValue") && target["getValue"].canInvoke()) {
            val p = DelegatedProperty(target, node.name, node.mutable, this)
            param.declareProperty(node.name, p)
        }
        throw LinUnsupportedOperationException("`$target` is not a valid property delegation target")
    }

    override fun visit(node: DestructuringVariableNode, param: Scope) = block {
        destructure(node.mutable, node.names, node.value.accept(this, param), param)
    }

    override fun visit(node: DeclareClassNode, param: Scope) = block {
        throw LinUnsupportedFeatureException("Class declarations are unsupported.")
    }

    override fun visit(node: DeclareEnumClassNode, param: Scope) = block {
        throw LinUnsupportedFeatureException("Enum class declarations are unsupported.")
    }

    override fun visit(node: DeclareInterfaceNode, param: Scope) = block {
        throw LinUnsupportedFeatureException("Interface declarations are unsupported.")
    }

    override fun visit(node: DeclareObjectNode, param: Scope) = block {
        val p = SimpleProperty(false)
        param.declareProperty(node.name, p)
        p.set(LObject(node.name, this, param, node.body))
    }

    override fun visit(node: DeclareFunctionNode, param: Scope) = block {
        val p = SimpleProperty(false)
        param.declareProperty(node.name, p)
        p.set(node.function.accept(this, param))
    }

    //endregion

    //region block nodes

    override fun visit(node: MultiNode, param: Scope) = block {
        for (child in node.list) {
            child.accept(this, param)
        }
    }

    override fun visit(node: MultiExpr, param: Scope): LObj {
        for (child in node.list) {
            child.accept(this, param)
        }
        return node.last.accept(this, param)
    }

    //endregion

    //region control flow

    override fun visit(node: IfNode, param: Scope) = block {
        val condition = node.condition.accept(this, BasicScope(param))
        if (condition !is LBoolean) throw LinIllegalArgumentException("'$condition' is not a Boolean")
        if (condition.value) node.thenBranch.accept(this, BasicScope(param))
        else node.elseBranch?.accept(this, BasicScope(param))
    }

    override fun visit(node: IfExpr, param: Scope): LObj {
        val condition = node.condition.accept(this, BasicScope(param))
        if (condition !is LBoolean) throw LinIllegalArgumentException("'$condition' is not a Boolean")
        return (if (condition.value) node.thenBranch else node.thenBranch).accept(this, BasicScope(param))
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
            if (condition !is LBoolean) throw LinIllegalArgumentException("'$condition' is not a Boolean")
            if (!condition.value) break
        }
    }

    override fun visit(node: WhileNode, param: Scope) = block {
        while (true) {
            val condition = node.condition.accept(this, BasicScope(param))
            if (condition !is LBoolean) throw LinIllegalArgumentException("'$condition' is not a Boolean")
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

    override fun visit(node: ForNode, param: Scope) = block {
        val iterable = node.iterable.accept(this, param)
        if (iterable is LinNativeIterable) {
            nativeIterate(node, iterable.iterator(), param)
        } else if (iterable.canGet("iterator")) {
            val iteratorFn = iterable["iterator"]
            if (iteratorFn.canInvoke()) {
                val iterator = iteratorFn.doCall()
                if (iterator.canGet("hasNext") && iterator.canGet("next")) {
                    iterate(node, iterator, param)
                } else throw LinIllegalArgumentException("'$iterator' does not implement 'next' and 'hasNext' functions")
            } else throw LinIllegalArgumentException("'$iterable' does not implement a 'iterator' function")
        } else if (iterable is LinNativeIterator) {
            nativeIterate(node, iterable, param)
        } else if (iterable.canGet("hasNext") && iterable.canGet("next")) {
            iterate(node, iterable, param)
        } else throw LinIllegalArgumentException("'$iterable' does not implement 'hasNext' and 'next' functions, or a 'iterator' function")
    }

    //endregion

    //region exceptional control flow

    override fun visit(node: ReturnExpr, param: Scope) = throw ReturnException(node.value.accept(this, param))

    override fun visit(node: ThrowExpr, param: Scope) = throw LinThrownException(node.value.accept(this, param))

    override fun visit(node: BreakExpr, param: Scope) = throw BreakException()

    override fun visit(node: ContinueExpr, param: Scope) = throw ContinueException()

    override fun visit(node: TryExpr, param: Scope): LObj {
        return try {
            node.tryBranch.accept(this, BasicScope(param))
        } catch (t: Throwable) {
            val catchBranch = node.catchBranch
            if (catchBranch == null || t !is LinCatchable) throw t
            val scope = BasicScope(param)
            if (catchBranch.caughtName != "_") {
                val p = SimpleProperty(false)
                p.set(t.thrown)
                scope.declareProperty(catchBranch.caughtName, p)
            }
            // FOR THE SAKE OF STACKTRACE PRESERVATION
            // IF THE OBJECT IS RE-THROWN
            // THROW ORIGINAL THROWABLE INSTEAD
            try {
                catchBranch.branch.accept(this, scope)
            } catch (tt: Throwable) {
                if (tt is LinCatchable && t.thrown == tt.thrown) throw t
                throw tt
            }
        } finally {
            node.finallyBranch?.accept(this, BasicScope(param))
        }
    }

    override fun visit(node: NotNullExpr, param: Scope): LObj {
        val value = node.value.accept(this, param)
        if (value == LNull) throw LinAssertionFailedException("value is null")
        return value
    }

    //endregion

    //region first-class objects

    override fun visit(node: ObjectExpr, param: Scope) = LObject(null, this, param, node.body)

    override fun visit(node: FunctionExpr, param: Scope) = CompiledFunction(param, node.parameters, node.body)

    override fun visit(node: LambdaExpr, param: Scope) = CompiledLambda(param, node.parameters, node.body)

    override fun visit(node: InitializerNode, param: Scope) = node.body.accept(this, param)

    //endregion

    //region invocations

    override fun visit(node: InvokeExpr, param: Scope): LObj {
        return node.target.accept(this, param)
            .canInvokeOrThrow()
            .doCall { node.arguments.map { it.accept(this, param) } }
    }

    override fun visit(node: InvokeLocalExpr, param: Scope): LObj {
        return param.findProperty(node.name)
            .orNullPointer(node.name)
            .getAllowedOrThrow(node.name).get()
            .canInvokeOrThrow()
            .doCall { node.arguments.map { it.accept(this, param) } }
    }

    override fun visit(node: InvokeMemberExpr, param: Scope): LObj {
        val parent = node.target.accept(this, param)
        if (node.nullSafe && parent == LNull) return LNull
        return parent.propertyOf(node.name)
            .orNullPointer(parent, node.name)
            .getAllowedOrThrow(parent, node.name).get()
            .canInvokeOrThrow()
            .doCall { node.arguments.map { it.accept(this, param) } }
    }

    //endregion

    //region subscript access

    override fun visit(node: SubscriptAccessExpr, param: Scope): LObj {
        val target = node.target.accept(this, param)
        if (target is LinNativeGet) {
            return target[node.arguments.map { it.accept(this, param) }]
        } else if (target.canGet("get")) {
            val getFn = target["get"]
            if (getFn.canInvoke()) {
                return getFn.doCall { node.arguments.map { it.accept(this, param) } }
            }
        }
        throw LinUnsupportedOperationException("'$target' does not support subscript get")
    }

    override fun visit(node: SubscriptAssignNode, param: Scope) = block {
        val target = node.target.accept(this, param)
        if (target is LinNativeSet) {
            target[node.arguments.map { it.accept(this, param) }] = node.value.accept(this, param)
            return@block
        } else if (target.canGet("set")) {
            val setFn = target["set"]
            if (setFn.canInvoke()) {
                setFn.doCall { node.arguments.map { it.accept(this, param) } + node.value.accept(this, param) }
                return@block
            }
        }
        throw LinUnsupportedOperationException("'$target' does not support subscript set")
    }

    //endregion

    //region binary, unary, unary pre/post-assign, binary assign operations

    override fun visit(node: BinaryOperation, param: Scope): LObj {
        val left = node.left.accept(this, param)
        when (node.operator) {
            BinaryOperationType.ADD -> {
                val right = node.right.accept(this, param)
                if (left is LNumber && right is LNumber) return box(plus(left.value, right.value))
                if (left is LString) {
                    if (right.canGet("toString")) {
                        val toStringFn = right["toString"]
                        if (toStringFn.canInvoke()) {
                            val str = toStringFn.doCall()
                            if (str !is LString) throw LinIllegalArgumentException("'$str' is not a String")
                            return LString(left.value + str.value)
                        }
                    }
                    return LString(left.value + right)
                }
                if (left is LChar) {
                    if (right is LNumber) return LChar(left.value + right.value.toInt())
                    return LString(left.value.toString() + right)
                }
                if (left.canGet("plus")) {
                    val opFn = left["plus"]
                    if (opFn.canInvoke()) {
                        return opFn.doCall { listOf(right) }
                    }
                }
                throw LinUnsupportedOperationException("Unsupported operation '$left + $right'")
            }
            BinaryOperationType.SUBTRACT -> {
                val right = node.right.accept(this, param)
                if (left is LNumber && right is LNumber) return box(minus(left.value, right.value))
                if (left.canGet("minus")) {
                    val opFn = left["minus"]
                    if (opFn.canInvoke()) {
                        return opFn.doCall { listOf(right) }
                    }
                }
                throw LinUnsupportedOperationException("Unsupported operation '$left - $right'")
            }
            BinaryOperationType.MULTIPLY -> {
                val right = node.right.accept(this, param)
                if (left is LNumber && right is LNumber) return box(times(left.value, right.value))
                if (left.canGet("times")) {
                    val opFn = left["times"]
                    if (opFn.canInvoke()) {
                        return opFn.doCall { listOf(right) }
                    }
                }
                throw LinUnsupportedOperationException("Unsupported operation '$left * $right'")
            }
            BinaryOperationType.DIVIDE -> {
                val right = node.right.accept(this, param)
                if (left is LNumber && right is LNumber) return box(div(left.value, right.value))
                if (left.canGet("div")) {
                    val opFn = left["div"]
                    if (opFn.canInvoke()) {
                        return opFn.doCall { listOf(right) }
                    }
                }
                throw LinUnsupportedOperationException("Unsupported operation '$left / $right'")
            }
            BinaryOperationType.REMAINING -> {
                val right = node.right.accept(this, param)
                if (left is LNumber && right is LNumber) return box(rem(left.value, right.value))
                if (left.canGet("rem")) {
                    val opFn = left["rem"]
                    if (opFn.canInvoke()) {
                        return opFn.doCall { listOf(right) }
                    }
                }
                throw LinUnsupportedOperationException("Unsupported operation '$left % $right'")
            }
            BinaryOperationType.EQUALS -> {
                val right = node.right.accept(this, param)
                if (left == LNull) {
                    return LBoolean.of(right == LNull)
                }
                if (left.canGet("equals")) {
                    val equalsFn = left["equals"]
                    if (equalsFn.canInvoke()) {
                        val bool = equalsFn.doCall { listOf(right) }
                        if (bool !is LBoolean) throw LinIllegalArgumentException("'$bool' is not a Boolean")
                        return bool
                    }
                }
                return LBoolean.of(left == right)
            }
            BinaryOperationType.NOT_EQUALS -> {
                val right = node.right.accept(this, param)
                if (left == LNull) {
                    return LBoolean.of(right != LNull)
                }
                if (left.canGet("equals")) {
                    val equalsFn = left["equals"]
                    if (equalsFn.canInvoke()) {
                        val bool = equalsFn.doCall { listOf(right) }
                        if (bool !is LBoolean) throw LinIllegalArgumentException("'$bool' is not a Boolean")
                        return !bool
                    }
                }
                return LBoolean.of(left != right)
            }
            BinaryOperationType.AND -> {
                if (left is LBoolean) {
                    if (!left.value) return left
                    val right = node.right.accept(this, param)
                    if (right is LBoolean) return right
                    throw LinUnsupportedOperationException("Unsupported operation '$left && $right'")
                }
                throw LinUnsupportedOperationException("Unsupported operation '$left && ...'")
            }
            BinaryOperationType.OR -> {
                if (left is LBoolean) {
                    if (left.value) return left
                    val right = node.right.accept(this, param)
                    if (right is LBoolean) return right
                    throw LinUnsupportedOperationException("Unsupported operation '$left || $right'")
                }
                throw LinUnsupportedOperationException("Unsupported operation '$left || ...'")
            }
            BinaryOperationType.LT -> {
                val right = node.right.accept(this, param)
                if (left is LNumber && right is LNumber) return LBoolean.of(left < right)
                if (left.canGet("compareTo")) {
                    val compareToFn = left["compareTo"]
                    if (compareToFn.canInvoke()) {
                        val number = compareToFn.doCall { listOf(right) }
                        if (number !is LNumber) throw LinIllegalArgumentException("'$number' is not a Number")
                        return LBoolean.of(number.value.toDouble() < 0)
                    }
                }
                throw LinUnsupportedOperationException("Unsupported operation '$left < $right'")
            }
            BinaryOperationType.LTE -> {
                val right = node.right.accept(this, param)
                if (left is LNumber && right is LNumber) {
                    return LBoolean.of(left <= right)
                }
                if (left.canGet("compareTo")) {
                    val compareToFn = left["compareTo"]
                    if (compareToFn.canInvoke()) {
                        val number = compareToFn.doCall { listOf(right) }
                        if (number !is LNumber) throw LinIllegalArgumentException("'$number' is not a Number")
                        return LBoolean.of(number.value.toDouble() <= 0)
                    }
                }
                throw LinUnsupportedOperationException("Unsupported operation '$left <= $right'")
            }
            BinaryOperationType.GT -> {
                val right = node.right.accept(this, param)
                if (left is LNumber && right is LNumber) {
                    return LBoolean.of(left > right)
                }
                if (left.canGet("compareTo")) {
                    val compareToFn = left["compareTo"]
                    if (compareToFn.canInvoke()) {
                        val number = compareToFn.doCall { listOf(right) }
                        if (number !is LNumber) throw LinIllegalArgumentException("'$number' is not a Number")
                        return LBoolean.of(number.value.toDouble() > 0)
                    }
                }
                throw LinUnsupportedOperationException("Unsupported operation '$left > $right'")
            }
            BinaryOperationType.GTE -> {
                val right = node.right.accept(this, param)
                if (left is LNumber && right is LNumber) {
                    return LBoolean.of(left >= right)
                }
                if (left.canGet("compareTo")) {
                    val compareToFn = left["compareTo"]
                    if (compareToFn.canInvoke()) {
                        val number = compareToFn.doCall { listOf(right) }
                        if (number !is LNumber) throw LinIllegalArgumentException("'$number' is not a Number")
                        return LBoolean.of(number.value.toDouble() >= 0)
                    }
                }
                throw LinUnsupportedOperationException("Unsupported operation '$left >= $right'")
            }
            BinaryOperationType.ELVIS -> {
                return if (left != LNull) left else node.right.accept(this, param)
            }
            BinaryOperationType.IN -> {
                val right = node.right.accept(this, param)
                if (left is LinNativeContains) {
                    return LBoolean.of(right in left)
                }
                if (left.canGet("contains")) {
                    val containsFn = left["contains"]
                    if (containsFn.canInvoke()) {
                        val bool = containsFn.doCall { listOf(right) }
                        if (bool !is LBoolean) throw LinIllegalArgumentException("'$bool' is not a Boolean")
                        return bool
                    }
                }
                throw LinUnsupportedOperationException("Unsupported operation '$left in $right'")
            }
            BinaryOperationType.RANGE -> {
                val right = node.right.accept(this, param)
                if (left is LinNativeRangeTo) {
                    return left.rangeTo(right)
                }
                if (left.canGet("rangeTo")) {
                    val rangeToFn = left["rangeTo"]
                    if (rangeToFn.canInvoke()) {
                        return rangeToFn.doCall { listOf(right) }
                    }
                }
                throw LinUnsupportedOperationException("Unsupported operation '$left..$right'")
            }
        }
    }

    override fun visit(node: UnaryOperation, param: Scope): LObj {
        val target = node.target.accept(this, param)
        when (node.operator) {
            UnaryOperationType.POSITIVE -> {
                if (target is LNumber) return box(unaryPlus(target.value))
                if (target.canGet("unaryPlus")) {
                    val opFn = target["unaryPlus"]
                    if (opFn.canInvoke()) return opFn.doCall()
                }
                throw LinUnsupportedOperationException("Unsupported operation '+$target'")
            }
            UnaryOperationType.NEGATIVE -> {
                if (target is LNumber) return box(unaryMinus(target.value))
                if (target.canGet("unaryMinus")) {
                    val opFn = target["unaryMinus"]
                    if (opFn.canInvoke()) return opFn.doCall()
                }
                throw LinUnsupportedOperationException("Unsupported operation '-$target'")
            }
            UnaryOperationType.NOT -> {
                if (target is LBoolean) return target.not()
                if (target.canGet("not")) {
                    val opFn = target["not"]
                    if (opFn.canInvoke()) return opFn.doCall()
                }
                throw LinUnsupportedOperationException("Unsupported operation '!$target'")
            }
        }
    }

    private fun applyUnaryAssignOperation(target: LObj, operator: UnaryAssignOperationType): LObj {
        when (operator) {
            UnaryAssignOperationType.INCREMENT -> {
                if (target is LNumber) return box(inc(target.value))
                if (target.canGet("inc")) {
                    val opFn = target["inc"]
                    if (opFn.canInvoke()) return opFn.doCall()
                }
                throw LinUnsupportedOperationException("Unsupported operation '$target++'")
            }
            UnaryAssignOperationType.DECREMENT -> {
                if (target is LNumber) return box(dec(target.value))
                if (target.canGet("dec")) {
                    val opFn = target["dec"]
                    if (opFn.canInvoke()) return opFn.doCall()
                }
                throw LinUnsupportedOperationException("Unsupported operation '$target--'")
            }
        }
    }

    override fun visit(node: PrefixAssignUnaryOperation, param: Scope): LObj {
        val property = node.target.resolve(this, param) ?: return LNull
        if (!property.getAllowed) throw LinIllegalAccessException("Property does not allow access.")
        if (!property.getAllowed) throw LinIllegalAccessException("Property does not allow assignment.")
        val target = property.get()
        val result = applyUnaryAssignOperation(target, node.operator)
        property.set(result)
        return result
    }

    override fun visit(node: PostfixAssignUnaryOperation, param: Scope): LObj {
        val property = node.target.resolve(this, param) ?: return LNull
        if (!property.getAllowed) throw LinIllegalAccessException("Property does not allow access.")
        if (!property.getAllowed) throw LinIllegalAccessException("Property does not allow assignment.")
        val target = property.get()
        val result = applyUnaryAssignOperation(target, node.operator)
        property.set(result)
        return target
    }

    override fun visit(node: AssignOperation, param: Scope) = block {
        val property = node.left.resolve(this, param) ?: return LNull
        if (!property.getAllowed) throw LinIllegalAccessException("Property does not allow access.")
        if (!property.getAllowed) throw LinIllegalAccessException("Property does not allow assignment.")
        val left = property.get()
        val right = node.right.accept(this, param)

        when (node.operator) {
            AssignOperationType.ADD_ASSIGN -> {
                when {
                    left is LNumber && right is LNumber -> property.set(box(plus(left.value, right.value)))
                    left is LString -> {
                        if (right.canGet("toString")) {
                            val toStringFn = right["toString"]
                            if (toStringFn.canInvoke()) {
                                val str = toStringFn.doCall()
                                if (str !is LString) throw LinIllegalArgumentException("'$str' is not a String")
                                property.set(LString(left.value + str.value))
                                return@block
                            }
                        }
                        property.set(LString(left.value + right))
                    }
                    left is LChar && right is LNumber -> property.set(LChar(left.value + right.value.toInt()))
                    else -> {
                        if (left.canGet("plusAssign")) {
                            val assignFn = left["plusAssign"]
                            if (assignFn.canInvoke()) {
                                assignFn.doCall()
                                return@block
                            }
                        }
                        if (left.canGet("plus")) {
                            val opFn = left["plus"]
                            if (opFn.canInvoke()) {
                                property.set(opFn.doCall { listOf(right) })
                                return@block
                            }
                        }
                        throw LinUnsupportedOperationException("Unsupported operation '$left += $right'")
                    }
                }
            }
            AssignOperationType.SUBTRACT_ASSIGN -> {
                when {
                    left is LNumber && right is LNumber -> property.set(box(minus(left.value, right.value)))
                    else -> {
                        if (left.canGet("minusAssign")) {
                            val assignFn = left["minusAssign"]
                            if (assignFn.canInvoke()) {
                                assignFn.doCall()
                                return@block
                            }
                        }
                        if (left.canGet("minus")) {
                            val opFn = left["minus"]
                            if (opFn.canInvoke()) {
                                property.set(opFn.doCall { listOf(right) })
                                return@block
                            }
                        }
                        throw LinUnsupportedOperationException("Unsupported operation '$left -= $right'")
                    }
                }
            }
            AssignOperationType.MULTIPLY_ASSIGN -> {
                when {
                    left is LNumber && right is LNumber -> property.set(box(times(left.value, right.value)))
                    else -> {
                        if (left.canGet("timesAssign")) {
                            val assignFn = left["timesAssign"]
                            if (assignFn.canInvoke()) {
                                assignFn.doCall()
                                return@block
                            }
                        }
                        if (left.canGet("times")) {
                            val opFn = left["times"]
                            if (opFn.canInvoke()) {
                                property.set(opFn.doCall { listOf(right) })
                                return@block
                            }
                        }
                        throw LinUnsupportedOperationException("Unsupported operation '$left *= $right'")
                    }
                }
            }
            AssignOperationType.DIVIDE_ASSIGN -> {
                when {
                    left is LNumber && right is LNumber -> property.set(box(div(left.value, right.value)))
                    else -> {
                        if (left.canGet("divAssign")) {
                            val assignFn = left["divAssign"]
                            if (assignFn.canInvoke()) {
                                assignFn.doCall()
                                return@block
                            }
                        }
                        if (left.canGet("div")) {
                            val opFn = left["div"]
                            if (opFn.canInvoke()) {
                                property.set(opFn.doCall { listOf(right) })
                                return@block
                            }
                        }
                        throw LinUnsupportedOperationException("Unsupported operation '$left /= $right'")
                    }
                }
            }
            AssignOperationType.REMAINING_ASSIGN -> {
                when {
                    left is LNumber && right is LNumber -> property.set(box(rem(left.value, right.value)))
                    else -> {
                        if (left.canGet("remAssign")) {
                            val assignFn = left["remAssign"]
                            if (assignFn.canInvoke()) {
                                assignFn.doCall()
                                return@block
                            }
                        }
                        if (left.canGet("rem")) {
                            val opFn = left["rem"]
                            if (opFn.canInvoke()) {
                                property.set(opFn.doCall { listOf(right) })
                                return@block
                            }
                        }
                        throw LinUnsupportedOperationException("Unsupported operation '$left %= $right'")
                    }
                }
            }
        }
    }

    //endregion

    //region inline functions

    private inline fun block(block: () -> Unit): LObj {
        block()
        return LUnit
    }

    private inline fun LObj.canInvokeOrThrow() = apply {
        if (!canInvoke()) throw LinUnsupportedOperationException("'$this' is not callable")
    }

    private inline fun Property.getAllowedOrThrow(parent: LObj, name: String) = apply {
        if (!getAllowed) throw LinIllegalAccessException("Property '$parent.$name' does not allow access.")
    }

    private inline fun Property.getAllowedOrThrow(name: String) = apply {
        if (!getAllowed) throw LinIllegalAccessException("Property '$name' does not allow access.")
    }

    private inline fun Property.setAllowedOrThrow(parent: LObj, name: String) = apply {
        if (!setAllowed) throw LinIllegalAccessException("Property '$parent.$name' does not allow assignments.")
    }

    private inline fun Property.setAllowedOrThrow(name: String) = apply {
        if (!setAllowed) throw LinIllegalAccessException("Property '$name' does not allow assignments.")
    }

    private inline fun Property?.orNullPointer(name: String): Property {
        return this ?: throw LinNullPointerException("There isn't a '$name' object associated in this context.")
    }

    private inline fun Property?.orNullPointer(parent: LObj, name: String): Property {
        return this ?: throw LinNullPointerException("There isn't a '$parent.$name' object associated in this context.")
    }

    private inline fun LObj.doCall(args: () -> List<LObj> = { emptyList() }): LObj {
        return callable().doCall(args)
    }

    private inline fun LinCall.doCall(args: () -> List<LObj> = { emptyList() }): LObj {
        if (this is LinDirectCall) return call(this@LinInterpreter, args())
        return this(args())
    }

    private inline fun destructure(mutable: Boolean, names: List<String>, target: LObj, param: Scope) {
        val properties = names.map { it to SimpleProperty(mutable) }.onEach { (s, p) -> param.declareProperty(s, p) }
        for (index in properties.indices) {
            val property = target.propertyOf("component$index")
                .orNullPointer(target, "component$index")
                .getAllowedOrThrow(target, "component$index")

            val componentFn = property.get().canInvokeOrThrow()
            properties[index].second.set(componentFn.doCall())
        }
    }

    private inline fun nativeIterate(node: ForNode, iterator: Iterator<LObj>, param: Scope) {
        for (each in iterator) {
            try {
                val child = UserScope(param)
                when (val v = node.variable) {
                    is ForNode.Variable.Destructured -> {
                        destructure(false, v.names, each, child)
                    }
                    is ForNode.Variable.Named -> {
                        child[v.name] = each
                    }
                }
                node.body.accept(this, child)
            } catch (_: BreakException) {
                break
            } catch (_: ContinueException) {
                continue
            }
        }
    }

    private inline fun iterate(node: ForNode, iterator: LObj, param: Scope) {
        val hasNextFn = iterator["hasNext"]
        val nextFn = iterator["next"]
        if (hasNextFn.canInvoke() && nextFn.canInvoke()) {
            val hasNextCall = hasNextFn.callable()
            val nextCall = nextFn.callable()
            while (true) {
                val hasNext = hasNextCall.doCall()
                if (hasNext !is LBoolean) throw LinIllegalArgumentException("'hasNext' returned '$hasNext', which is not a Boolean")
                if (!hasNext.value) {
                    break
                }
                val next = nextCall.doCall()
                try {
                    val child = UserScope(param)
                    when (val v = node.variable) {
                        is ForNode.Variable.Destructured -> {
                            destructure(false, v.names, next, child)
                        }
                        is ForNode.Variable.Named -> {
                            child[v.name] = next
                        }
                    }
                    node.body.accept(this, child)
                } catch (_: BreakException) {
                    break
                } catch (_: ContinueException) {
                    continue
                }
            }
        } else throw LinIllegalArgumentException("'$iterator' does not implement 'next' and 'hasNext' functions")
    }

    //endregion
}

