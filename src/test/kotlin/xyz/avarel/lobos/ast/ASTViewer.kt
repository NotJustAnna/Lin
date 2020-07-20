package xyz.avarel.lobos.ast

import xyz.avarel.lobos.ast.expr.Expr
import xyz.avarel.lobos.ast.expr.ExprVisitor
import xyz.avarel.lobos.ast.expr.access.*
import xyz.avarel.lobos.ast.expr.declarations.*
import xyz.avarel.lobos.ast.expr.files.FileModuleExpr
import xyz.avarel.lobos.ast.expr.files.FolderModuleExpr
import xyz.avarel.lobos.ast.expr.invoke.InvokeExpr
import xyz.avarel.lobos.ast.expr.invoke.InvokeLocalExpr
import xyz.avarel.lobos.ast.expr.invoke.InvokeMemberExpr
import xyz.avarel.lobos.ast.expr.misc.*
import xyz.avarel.lobos.ast.expr.nodes.*
import xyz.avarel.lobos.ast.expr.ops.BinaryOperation
import xyz.avarel.lobos.ast.expr.ops.UnaryOperation

class ASTViewer(val buf: StringBuilder, val indent: String = "", val isTail: Boolean) : ExprVisitor<Unit> {
    fun Expr.ast(indent: String = this@ASTViewer.indent + if (isTail) "    " else "│   ", tail: Boolean) =
        accept(ASTViewer(buf, indent, tail))

    fun Expr.astLabel(
        label: String,
        indent: String = this@ASTViewer.indent + if (isTail) "    " else "│   ",
        tail: Boolean
    ) {
        listOf(this).astGroup(label, indent, tail)
    }

    fun List<Expr>.astGroup(
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

    override fun visit(expr: UseExpr) {
        base("use")
        label(expr.list.joinToString("::"), tail = true)
    }

    override fun visit(expr: FileModuleExpr) {
        base("file module")
        label("name: ${expr.name}", expr.declarationsAST.isEmpty())

        expr.declarationsAST.let { d ->
            d.modules.astGroup("submodules", tail = d.functions.isEmpty() && d.variables.isEmpty())
            d.functions.astGroup("functions", tail = d.variables.isEmpty())
            d.variables.astGroup("variables", tail = true)
        }
    }

    override fun visit(expr: FolderModuleExpr) {
        base("folder module")
        label("name: ${expr.name}", expr.folderModules.isNotEmpty() && expr.fileModules.isNotEmpty())

        if (expr.folderModules.isNotEmpty()) expr.folderModules.astGroup("folders", tail = expr.fileModules.isEmpty())
        if (expr.fileModules.isNotEmpty()) expr.fileModules.astGroup("files", tail = true)
    }

    override fun visit(expr: IdentExpr) = base("reference ${expr.name}")

    override fun visit(expr: NullExpr) = base("null ref")

    override fun visit(expr: InvalidExpr) = base("[Invalid expression.]")

    override fun visit(expr: I32Expr) = base("i32: ${expr.value}")

    override fun visit(expr: I64Expr) = base("i64: ${expr.value}")

    override fun visit(expr: F64Expr) = base("f64: ${expr.value}")

    override fun visit(expr: StringExpr) = base("str: \"${expr.value}\"")

    override fun visit(expr: BooleanExpr) = base(expr.value.toString())

    override fun visit(expr: MultiExpr) {
        for (i in 0 until expr.list.size) {
            expr.list[i].ast(indent, i == expr.list.lastIndex)
        }
    }

    override fun visit(expr: DeclareModuleExpr) {
        base("local module")

        label("name: ${expr.name}", false)

        expr.declarationsAST.let { d ->
            d.modules.astGroup("submodules", tail = d.functions.isEmpty() && d.variables.isEmpty())
            d.functions.astGroup("functions", tail = d.variables.isEmpty())
            d.variables.astGroup("variables", tail = true)
        }
    }

    override fun visit(expr: DeclareFunctionExpr) {
        base("function")

        label("name: ${expr.name}", false)

        if (expr.generics.isNotEmpty()) {
            label(expr.generics.joinToString(prefix = "generics: <", postfix = ">"), false)
        }

        label(expr.arguments.joinToString(prefix = "arguments: (", postfix = ")"), false)
        label("return: ${expr.returnType}", false)
        expr.body.astLabel("body", tail = true)
    }

    override fun visit(expr: DeclareLetExpr) {
        base("let")

        label("${expr.pattern}", tail = false)
        expr.value.astLabel("value", tail = true)
    }

    override fun visit(expr: TypeAliasExpr) {
        base("typealias")

        label("name: ${expr.name}", false)

        if (expr.generics.isNotEmpty()) {
            label(expr.generics.joinToString(prefix = "generics: <", postfix = ">"), false)
        }

        label("type: ${expr.type}", true)
    }

    override fun visit(expr: ExternalModuleExpr) {
        base("external module")

        label("name: ${expr.name}", false)

        expr.declarationsAST.let { d ->
            d.modules.astGroup("submodules", tail = d.functions.isEmpty() && d.variables.isEmpty())
            d.functions.astGroup("functions", tail = d.variables.isEmpty())
            d.variables.astGroup("variables", tail = true)
        }
    }

    override fun visit(expr: ExternalLetExpr) {
        base("external let")

        label("name: ${expr.name}", true)
    }

    override fun visit(expr: ExternalFunctionExpr) {
        base("external function")

        label("name: ${expr.name}", true)

        if (expr.generics.isNotEmpty()) {
            label(expr.generics.joinToString(prefix = "generics: <", postfix = ">"), false)
        }

        label(expr.arguments.joinToString(prefix = "arguments: (", postfix = ")"), false)
        label("return: ${expr.returnType}", true)
    }

    override fun visit(expr: AssignExpr) {
        base("assign ${expr.name}")

        expr.value.ast(tail = true)
    }

    override fun visit(expr: ClosureExpr) {
        base("closure")

        label(expr.arguments.joinToString(prefix = "arguments: (", postfix = ")"), false)
        expr.body.astLabel("body", tail = true)
    }

    override fun visit(expr: TupleExpr) {
        base("tuple")

        for (i in 0 until expr.list.size) {
            expr.list[i].ast(tail = i == expr.list.lastIndex)
        }
    }

    override fun visit(expr: ListLiteralExpr) {
        base("list literal")

        for (i in 0 until expr.list.size) {
            expr.list[i].ast(tail = i == expr.list.lastIndex)
        }
    }

    override fun visit(expr: MapLiteralExpr) {
        base("map literal")

        for ((i, entry) in expr.map.entries.withIndex()) {
            listOf(entry.key, entry.value).astGroup("entry $i", tail = i == expr.map.size - 1)
        }
    }

    override fun visit(expr: TemplateExpr) {
        base("template")

        expr.target.astLabel("target", tail = false)
        label(expr.typeArguments.joinToString(prefix = "type arguments: <", postfix = ">"), true)
    }

    override fun visit(expr: InvokeExpr) {
        base("invoke")

        expr.target.astLabel("target", tail = expr.arguments.isEmpty())
        expr.arguments.astGroup("arguments", tail = true)
    }

    override fun visit(expr: InvokeLocalExpr) {
        base("invoke local")

        label("name: ${expr.name}", tail = false)
        expr.arguments.astGroup("arguments", tail = true)
    }

    override fun visit(expr: InvokeMemberExpr) {
        base("invoke member function")

        expr.target.astLabel("target", tail = false)
        label("name: ${expr.name}", expr.arguments.isEmpty())
        expr.arguments.astGroup("arguments", tail = true)
    }

    override fun visit(expr: UnaryOperation) {
        base("unary ${expr.operator}")

        expr.target.ast(tail = true)
    }

    override fun visit(expr: BinaryOperation) {
        base("binary ${expr.operator}")

        expr.left.ast(tail = false)
        expr.right.ast(tail = true)
    }

    override fun visit(expr: ReturnExpr) {
        base("return")

        expr.value.ast(tail = true)
    }

    override fun visit(expr: IfExpr) {
        base("if")

        expr.condition.astLabel("condition", tail = false)
        expr.thenBranch.astLabel("then", tail = expr.elseBranch == null)

        if (expr.elseBranch != null) {
            expr.elseBranch.astLabel("else", tail = true)
        }
    }

    override fun visit(expr: WhileExpr) {
        base("while")

        expr.condition.astLabel("condition", tail = false)
        expr.body.astLabel("body", tail = true)
    }

    override fun visit(expr: SubscriptAccessExpr) {
        base("subscript access")

        expr.target.astLabel("target", tail = false)
        expr.index.astLabel("index", tail = true)
    }

    override fun visit(expr: SubscriptAssignExpr) {
        base("subscript assign")

        expr.target.astLabel("target", tail = false)
        expr.index.astLabel("index", tail = false)
        expr.value.astLabel("value", tail = true)
    }

    override fun visit(expr: TupleIndexAccessExpr) {
        base("tuple index access")

        expr.target.astLabel("target", tail = false)
        label("index: ${expr.index}", true)
    }

    override fun visit(expr: PropertyAccessExpr) {
        base("property access")

        expr.target.astLabel("target", tail = false)
        label("name: ${expr.name}", true)
    }

    override fun visit(expr: PropertyAssignExpr) {
        base("property access")

        expr.target.astLabel("target", tail = false)
        label("name: ${expr.name}", true)
        expr.value.astLabel("value", tail = true)
    }
}