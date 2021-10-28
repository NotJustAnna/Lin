package net.notjustanna.lin.ast.viewer

import net.notjustanna.lin.ast.node.InvalidNode
import net.notjustanna.lin.ast.node.MultiExpr
import net.notjustanna.lin.ast.node.MultiNode
import net.notjustanna.lin.ast.node.access.*
import net.notjustanna.lin.ast.node.control.*
import net.notjustanna.lin.ast.node.control.optimization.LoopNode
import net.notjustanna.lin.ast.node.control.optimization.ScopeExpr
import net.notjustanna.lin.ast.node.control.optimization.ScopeNode
import net.notjustanna.lin.ast.node.declare.DeclareFunctionExpr
import net.notjustanna.lin.ast.node.declare.DeclareVariableNode
import net.notjustanna.lin.ast.node.invoke.InvokeExpr
import net.notjustanna.lin.ast.node.invoke.InvokeExtensionExpr
import net.notjustanna.lin.ast.node.invoke.InvokeLocalExpr
import net.notjustanna.lin.ast.node.invoke.InvokeMemberExpr
import net.notjustanna.lin.ast.node.misc.BinaryOperation
import net.notjustanna.lin.ast.node.misc.EnsureNotNullExpr
import net.notjustanna.lin.ast.node.misc.TypeofExpr
import net.notjustanna.lin.ast.node.misc.UnaryOperation
import net.notjustanna.lin.ast.node.value.*
import net.notjustanna.lin.ast.visitor.NodeVisitor

class NodeTreePrinter(private val builder: StringBuilder = StringBuilder()) : NodeVisitor, Appendable by builder {
    private var indent = 0
    private var name: String? = null

    override fun visitArrayExpr(node: ArrayExpr) {
        appendIndent()
        appendName()
        appendLine("ArrayExpr ${node.section} {")
        indenting {
            var i = 0
            for (each in node.value) {
                name = "entry ${i++}"
                each.accept(this)
            }
        }
        appendIndent()
        appendLine('}')
    }

    override fun visitAssignNode(node: AssignNode) {
        TODO("Not yet implemented")
    }

    override fun visitBinaryOperation(node: BinaryOperation) {
        TODO("Not yet implemented")
    }

    override fun visitBooleanExpr(node: BooleanExpr) {
        appendIndent()
        appendName()
        appendLine("BooleanExpr ${node.section} = ${node.value}")
    }

    override fun visitBreakExpr(node: BreakExpr) {
        TODO("Not yet implemented")
    }

    override fun visitCharExpr(node: CharExpr) {
        appendIndent()
        appendName()
        appendLine("CharExpr ${node.section} = '${node.value}'")
    }

    override fun visitContinueExpr(node: ContinueExpr) {
        TODO("Not yet implemented")
    }

    override fun visitDeclareFunctionExpr(node: DeclareFunctionExpr) {
        TODO("Not yet implemented")
    }

    override fun visitDeclareVariableNode(node: DeclareVariableNode) {
        TODO("Not yet implemented")
    }

    override fun visitDoWhileNode(node: DoWhileNode) {
        TODO("Not yet implemented")
    }

    override fun visitDoubleExpr(node: DoubleExpr) {
        appendIndent()
        appendName()
        appendLine("DoubleExpr ${node.section} = ${node.value}")
    }

    override fun visitEnsureNotNullExpr(node: EnsureNotNullExpr) {
        TODO("Not yet implemented")
    }

    override fun visitFloatExpr(node: FloatExpr) {
        appendIndent()
        appendName()
        appendLine("FloatExpr ${node.section} = ${node.value}")
    }

    override fun visitForNode(node: ForNode) {
        TODO("Not yet implemented")
    }

    override fun visitFunctionExpr(node: FunctionExpr) {
        TODO("Not yet implemented")
    }

    override fun visitIdentifierExpr(node: IdentifierExpr) {
        appendIndent()
        appendName()
        appendLine("IdentifierExpr ${node.section} = ${node.name}")
    }

    override fun visitIfExpr(node: IfExpr) {
        TODO("Not yet implemented")
    }

    override fun visitIfNode(node: IfNode) {
        TODO("Not yet implemented")
    }

    override fun visitIntExpr(node: IntExpr) {
        appendIndent()
        appendName()
        appendLine("IntExpr ${node.section} = ${node.value}")
    }

    override fun visitInvalidNode(node: InvalidNode) {
        TODO("Not yet implemented")
    }

    override fun visitInvokeExpr(node: InvokeExpr) {
        TODO("Not yet implemented")
    }

    override fun visitInvokeExtensionExpr(node: InvokeExtensionExpr) {
        TODO("Not yet implemented")
    }

    override fun visitInvokeLocalExpr(node: InvokeLocalExpr) {
        TODO("Not yet implemented")
    }

    override fun visitInvokeMemberExpr(node: InvokeMemberExpr) {
        TODO("Not yet implemented")
    }

    override fun visitLongExpr(node: LongExpr) {
        appendIndent()
        appendName()
        appendLine("LongExpr ${node.section} = ${node.value}")
    }

    override fun visitLoopNode(node: LoopNode) {
        TODO("Not yet implemented")
    }

    override fun visitMultiExpr(node: MultiExpr) {
        appendIndent()
        appendName()
        appendLine("MultiExpr ${node.section} {")
        indenting {
            var i = 0
            for (each in node.list) {
                name = "node ${i++}"
                each.accept(this)
            }
            name = "node $i"
            node.last.accept(this)
        }
        appendLine('}')
    }

    override fun visitMultiNode(node: MultiNode) {
        TODO("Not yet implemented")
    }

    override fun visitNullExpr(node: NullExpr) {
        appendIndent()
        appendName()
        appendLine("NullExpr ${node.section}")
    }

    override fun visitObjectExpr(node: ObjectExpr) {
        appendIndent()
        appendName()
        appendLine("ObjectExpr ${node.section} {")
        indenting {
            var i = 0
            for ((key, value) in node.value) {
                name = (i++).toString()
                appendIndent()
                appendLine("entry $i: {")
                indenting {
                    name = "key"
                    key.accept(this)
                    name = "value"
                    value.accept(this)
                }
                appendIndent()
                appendLine('}')
            }
        }
        appendIndent()
        appendLine('}')
    }

    override fun visitPropertyAccessExpr(node: PropertyAccessExpr) {
        TODO("Not yet implemented")
    }

    override fun visitPropertyAssignNode(node: PropertyAssignNode) {
        TODO("Not yet implemented")
    }

    override fun visitReturnExpr(node: ReturnExpr) {
        TODO("Not yet implemented")
    }

    override fun visitScopeExpr(node: ScopeExpr) {
        TODO("Not yet implemented")
    }

    override fun visitScopeNode(node: ScopeNode) {
        TODO("Not yet implemented")
    }

    override fun visitStringExpr(node: StringExpr) {
        appendIndent()
        appendName()
        appendLine("StringExpr ${node.section} = \"${node.value}\"")
    }

    override fun visitSubscriptAccessExpr(node: SubscriptAccessExpr) {
        TODO("Not yet implemented")
    }

    override fun visitSubscriptAssignNode(node: SubscriptAssignNode) {
        TODO("Not yet implemented")
    }

    override fun visitThisExpr(node: ThisExpr) {
        appendIndent()
        appendName()
        appendLine("ThisExpr ${node.section}")
    }

    override fun visitThrowExpr(node: ThrowExpr) {
        TODO("Not yet implemented")
    }

    override fun visitTryExpr(node: TryExpr) {
        TODO("Not yet implemented")
    }

    override fun visitTypeofExpr(node: TypeofExpr) {
        appendIndent()
        appendName()
        appendLine("TypeofExpr ${node.section} {")
        indenting {
            name = "value"
            node.value.accept(this)
        }
        appendLine('}')
    }

    override fun visitUnaryOperation(node: UnaryOperation) {
        appendIndent()
        appendName()
        appendLine("UnaryOperation ${node.operator} ${node.section} {")
        indenting {
            name = "target"
            node.target.accept(this)
        }
        appendLine('}')
    }

    override fun visitUnitExpr(node: UnitExpr) {
        appendIndent()
        appendName()
        appendLine("UnitExpr ${node.section}")
    }

    override fun visitWhileNode(node: WhileNode) {
        TODO("Not yet implemented")
    }

    private fun appendIndent() {
        for (ignored in 0 until indent) {
            append(' ')
        }
    }

    private fun appendName() {
        if (name != null) {
            append(name)
            append(": ")
        }
    }

    private inline fun indenting(block: () -> Unit) {
        indent += 2
        block()
        indent -= 2
    }
}
