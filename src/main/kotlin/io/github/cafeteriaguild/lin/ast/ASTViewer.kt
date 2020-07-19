package io.github.cafeteriaguild.lin.ast

import io.github.cafeteriaguild.lin.ast.expr.Node
import io.github.cafeteriaguild.lin.ast.expr.NodeVisitor
import io.github.cafeteriaguild.lin.ast.expr.access.*
import io.github.cafeteriaguild.lin.ast.expr.declarations.*
import io.github.cafeteriaguild.lin.ast.expr.invoke.InvokeExpr
import io.github.cafeteriaguild.lin.ast.expr.invoke.InvokeLocalExpr
import io.github.cafeteriaguild.lin.ast.expr.invoke.InvokeMemberExpr
import io.github.cafeteriaguild.lin.ast.expr.misc.*
import io.github.cafeteriaguild.lin.ast.expr.nodes.*
import io.github.cafeteriaguild.lin.ast.expr.ops.*

class ASTViewer(val buf: StringBuilder, val indent: String = "", val isTail: Boolean) : NodeVisitor<Unit> {
    fun Node.ast(indent: String = this@ASTViewer.indent + if (isTail) "    " else "│   ", tail: Boolean) =
        accept(ASTViewer(buf, indent, tail))

    fun Node.astLabel(
        label: String,
        indent: String = this@ASTViewer.indent + if (isTail) "    " else "│   ",
        tail: Boolean
    ) {
        listOf(this).astGroup(label, indent, tail)
    }

    fun List<Node>.astGroup(
        label: String,
        indent: String = this@ASTViewer.indent + if (isTail) "    " else "│   ",
        tail: Boolean
    ) {
        if (isEmpty()) return

        label("$label:", tail)

        for (i in indices) {
            this[i].ast(indent = indent + if (tail) "    " else "│   ", tail = i == lastIndex)
        }
    }

    private fun base(string: String) {
        buf.apply {
            append(indent)
            append(if (isTail) "└── " else "├── ")
            append(string)
            append('\n')
        }
    }

    private fun label(string: String, tail: Boolean) {
        buf.apply {
            append(indent)
            append(if (isTail) "    " else "│   ")
            append(if (tail) "└── " else "├── ")
            append(string)
            append('\n')
        }
    }

//    override fun visit(expr: UseExpr) {
//        base("use")
//        label(expr.list.joinToString("::"), tail = true)
//    }

//    override fun visit(expr: FileModuleExpr) {
//        base("file module")
//        label("name: ${expr.name}", expr.declarationsAST.isEmpty())
//
//        expr.declarationsAST.let { d ->
//            d.modules.astGroup("submodules", tail = d.functions.isEmpty() && d.variables.isEmpty())
//            d.functions.astGroup("functions", tail = d.variables.isEmpty())
//            d.variables.astGroup("variables", tail = true)
//        }
//    }

//    override fun visit(expr: FolderModuleExpr) {
//        base("folder module")
//        label("name: ${expr.name}", expr.folderModules.isNotEmpty() && expr.fileModules.isNotEmpty())
//
//        if (expr.folderModules.isNotEmpty())  expr.folderModules.astGroup("folders", tail = expr.fileModules.isEmpty())
//        if (expr.fileModules.isNotEmpty()) expr.fileModules.astGroup("files", tail = true)
//    }

    override fun visit(expr: InvalidNode) {
        base("[Invalid expression.]")
        expr.children.astGroup("children", tail = false)
        expr.errors.run {
            for (i in indices) {
                label(this[i].toString(), i == lastIndex)
            }
        }
    }

    override fun visit(expr: IdentifierExpr) = base("reference ${expr.name}")

    override fun visit(expr: NullExpr) = base("null ref")

    override fun visit(expr: IntExpr) = base("Int: ${expr.value}")

    override fun visit(expr: LongExpr) = base("Long: ${expr.value}")

    override fun visit(expr: FloatExpr) = base("Float: ${expr.value}")

    override fun visit(expr: DoubleExpr) = base("Double: ${expr.value}")

    override fun visit(expr: CharExpr) = base("Char: \'${expr.value}\'")

    override fun visit(expr: StringExpr) = base("String: \"${expr.value}\"")

    override fun visit(expr: BooleanExpr) = base(expr.value.toString())

    override fun visit(expr: MultiNode) {
        for (i in expr.list.indices) {
            expr.list[i].ast(indent, i == expr.list.lastIndex)
        }
    }

    override fun visit(expr: MultiExpr) {
        for (i in expr.list.indices) {
            expr.list[i].ast(indent, false)
        }
        expr.last.ast(indent, true)
    }

//    override fun visit(expr: DeclareModuleExpr) {
//        base("local module")
//
//        label("name: ${expr.name}", false)
//
//        expr.declarationsAST.let { d ->
//            d.modules.astGroup("submodules", tail = d.functions.isEmpty() && d.variables.isEmpty())
//            d.functions.astGroup("functions", tail = d.variables.isEmpty())
//            d.variables.astGroup("variables", tail = true)
//        }
//    }

    override fun visit(expr: DeclareObjectNode) {
        base("declare ${if (expr.isCompanion) "companion object" else "object"}")
        label("name: ${expr.name}", false)
        expr.obj.astLabel("object", tail = true)
    }

    override fun visit(expr: DeclareFunctionNode) {
        base("declare function")
        label("name: ${expr.name}", false)
        expr.function.astLabel("function", tail = true)
    }

    override fun visit(expr: DeclareVariableNode) {
        base(if (expr.mutable) "var" else "val")
        label("name: ${expr.name}", tail = expr.value == null)
        expr.value?.astLabel("value", tail = true)
    }

    override fun visit(expr: DestructuringVariableNode) {
        base(if (expr.mutable) "var" else "val")
        label("destructuring: ${expr.names.joinToString(", ", "(", ")")}", tail = false)
        expr.value.astLabel("value", tail = true)
    }

    override fun visit(expr: ObjectExpr) {
        base("object")
        expr.body.astGroup("body", tail = true)
    }

    override fun visit(expr: FunctionExpr) {
        base("function")
        val (names, values) = expr.parameters.map { it.name to it.value }.unzip()
        if (names.isNotEmpty()) {
            label("parameters: ${names.joinToString(", ")}", tail = false)
        }
        values.filterNotNull().astGroup("default values", tail = false)
        expr.body.astLabel("body", tail = true)
    }

    override fun visit(expr: LambdaExpr) {
        base("lambda")
        val parameters = expr.parameters
        if (parameters.isNotEmpty()) {
            label("parameters: ${parameters.joinToString(", ")}", tail = false)
        }
        expr.body.astLabel("body", tail = true)
    }

    override fun visit(expr: InitializerNode) {
        base("init")
        expr.body.astLabel("body", tail = true)
    }

//    override fun visit(expr: TypeAliasExpr) {
//        base("typealias")
//
//        label("name: ${expr.name}", false)
//
//        if (expr.generics.isNotEmpty()) {
//            label(expr.generics.joinToString(prefix = "generics: <", postfix = ">"), false)
//        }
//
//        label("type: ${expr.type}", true)
//    }

//    override fun visit(expr: ExternalModuleExpr) {
//        base("external module")
//
//        label("name: ${expr.name}", false)
//
//        expr.declarationsAST.let { d ->
//            d.modules.astGroup("submodules", tail = d.functions.isEmpty() && d.variables.isEmpty())
//            d.functions.astGroup("functions", tail = d.variables.isEmpty())
//            d.variables.astGroup("variables", tail = true)
//        }
//    }

//    override fun visit(expr: ExternalLetExpr) {
//        base("external let")
//
//        label("name: ${expr.name}", true)
//    }

//    override fun visit(expr: ExternalFunctionExpr) {
//        base("external function")
//
//        label("name: ${expr.name}", true)
//
//        if (expr.generics.isNotEmpty()) {
//            label(expr.generics.joinToString(prefix = "generics: <", postfix = ">"), false)
//        }
//
//        label(expr.arguments.joinToString(prefix = "arguments: (", postfix = ")"), false)
//        label("return: ${expr.returnType}", true)
//    }

    override fun visit(expr: AssignNode) {
        base("assign ${expr.name}")

        expr.value.ast(tail = true)
    }

//    override fun visit(expr: ClosureExpr) {
//        base("closure")
//
//        label(expr.arguments.joinToString(prefix = "arguments: (", postfix = ")"), false)
//        expr.body.astLabel("body", tail = true)
//    }

    override fun visit(expr: UnitExpr) {
        base("unit")
    }

//    override fun visit(expr: TupleExpr) {
//        base("tuple")
//
//        for (i in 0 until expr.list.size) {
//            expr.list[i].ast(tail = i == expr.list.lastIndex)
//        }
//    }

//    override fun visit(expr: ListLiteralExpr) {
//        base("list literal")
//
//        for (i in 0 until expr.list.size) {
//            expr.list[i].ast(tail = i == expr.list.lastIndex)
//        }
//    }

//    override fun visit(expr: MapLiteralExpr) {
//        base("map literal")
//
//        for ((i, entry) in expr.map.entries.withIndex()) {
//            listOf(entry.key, entry.value).astGroup("entry $i", tail = i == expr.map.size - 1)
//        }
//    }

//    override fun visit(expr: TemplateExpr) {
//        base("template")
//
//        expr.target.astLabel("target", tail = false)
//        label(expr.typeArguments.joinToString(prefix = "type arguments: <", postfix = ">"), true)
//    }

    override fun visit(expr: InvokeExpr) {
        base("invoke")

        expr.target.astLabel("target", tail = expr.arguments.isEmpty())
        expr.arguments.astGroup("arguments", tail = true)
    }

    override fun visit(expr: InvokeLocalExpr) {
        base("invoke local")

        label("name: ${expr.name}", tail = expr.arguments.isEmpty())
        expr.arguments.astGroup("arguments", tail = true)
    }

    override fun visit(expr: InvokeMemberExpr) {
        base("invoke member function")

        expr.target.astLabel("target", tail = false)
        label("null safe: ${expr.nullSafe}", tail = false)
        label("name: ${expr.name}", expr.arguments.isEmpty())
        expr.arguments.astGroup("arguments", tail = true)
    }

    override fun visit(expr: UnaryOperation) {
        base("unary ${expr.operator}")

        expr.target.ast(tail = true)
    }

    override fun visit(expr: PreAssignUnaryOperation) {
        base("pre-assign unary ${expr.operator}")

        expr.target.ast(tail = true)
    }

    override fun visit(expr: PostAssignUnaryOperation) {
        base("post-assign unary ${expr.operator}")

        expr.target.ast(tail = true)
    }

    override fun visit(expr: BinaryOperation) {
        base("binary ${expr.operator}")

        expr.left.ast(tail = false)
        expr.right.ast(tail = true)
    }

    override fun visit(expr: AssignOperation) {
        base("assign operation ${expr.operator}")

        expr.left.ast(tail = false)
        expr.right.ast(tail = true)
    }

    override fun visit(expr: ReturnExpr) {
        base("return")

        expr.value.ast(tail = true)
    }

    override fun visit(expr: ThrowExpr) {
        base("throw")

        expr.value.ast(tail = true)
    }

    override fun visit(expr: NotNullExpr) {
        base("not null")

        expr.value.ast(tail = true)
    }

    override fun visit(expr: IfExpr) {
        base("if expr")

        expr.condition.astLabel("condition", tail = false)
        expr.thenBranch.astLabel("then", tail = expr.elseBranch == null)

        if (expr.elseBranch != null) {
            expr.elseBranch.astLabel("else", tail = true)
        }
    }

    override fun visit(expr: IfNode) {
        base("if node")

        expr.condition.astLabel("condition", tail = false)
        expr.thenBranch.astLabel("then", tail = false)
        expr.elseBranch.astLabel("else", tail = true)
    }

    override fun visit(expr: DoWhileNode) {
        base("do-while")

        expr.body.astLabel("body", tail = false)
        expr.condition.astLabel("condition", tail = true)
    }

    override fun visit(expr: WhileNode) {
        base("while")

        expr.condition.astLabel("condition", tail = false)
        expr.body.astLabel("body", tail = true)
    }

    override fun visit(expr: ForNode) {
        base("for")

        label("variable name: ${expr.variableName}", tail = false)
        expr.iterable.astLabel("iterable", tail = false)
        expr.body.astLabel("body", tail = true)
    }

    override fun visit(expr: SubscriptAccessExpr) {
        base("subscript access")

        expr.target.astLabel("target", tail = false)
        expr.arguments.astGroup("arguments", tail = true)
    }

    override fun visit(expr: SubscriptAssignNode) {
        base("subscript assign")

        expr.target.astLabel("target", tail = false)
        expr.arguments.astGroup("arguments", tail = false)
        expr.value.astLabel("value", tail = true)
    }

//    override fun visit(expr: TupleIndexAccessExpr) {
//        base("tuple index access")
//
//        expr.target.astLabel("target", tail = false)
//        label("index: ${expr.index}", true)
//    }

    override fun visit(expr: PropertyAccessExpr) {
        base("property access")

        expr.target.astLabel("target", tail = false)
        label("null safe: ${expr.nullSafe}", tail = false)
        label("name: ${expr.name}", true)
    }

    override fun visit(expr: PropertyAssignNode) {
        base("property access")

        expr.target.astLabel("target", tail = false)
        label("null safe: ${expr.nullSafe}", tail = false)
        label("name: ${expr.name}", true)
        expr.value.astLabel("value", tail = true)
    }
}