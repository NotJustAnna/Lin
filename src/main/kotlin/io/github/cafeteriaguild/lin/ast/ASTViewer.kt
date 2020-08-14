package io.github.cafeteriaguild.lin.ast

import io.github.cafeteriaguild.lin.ast.node.Node
import io.github.cafeteriaguild.lin.ast.node.NodeVisitor
import io.github.cafeteriaguild.lin.ast.node.access.*
import io.github.cafeteriaguild.lin.ast.node.declarations.*
import io.github.cafeteriaguild.lin.ast.node.invoke.InvokeExpr
import io.github.cafeteriaguild.lin.ast.node.invoke.InvokeLocalExpr
import io.github.cafeteriaguild.lin.ast.node.invoke.InvokeMemberExpr
import io.github.cafeteriaguild.lin.ast.node.misc.*
import io.github.cafeteriaguild.lin.ast.node.nodes.*
import io.github.cafeteriaguild.lin.ast.node.ops.*

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

//    override fun visit(node: UseExpr) {
//        base("use")
//        label(node.list.joinToString("::"), tail = true)
//    }

//    override fun visit(node: FileModuleExpr) {
//        base("file module")
//        label("name: ${node.name}", node.declarationsAST.isEmpty())
//
//        node.declarationsAST.let { d ->
//            d.modules.astGroup("submodules", tail = d.functions.isEmpty() && d.variables.isEmpty())
//            d.functions.astGroup("functions", tail = d.variables.isEmpty())
//            d.variables.astGroup("variables", tail = true)
//        }
//    }

//    override fun visit(node: FolderModuleExpr) {
//        base("folder module")
//        label("name: ${node.name}", node.folderModules.isNotEmpty() && node.fileModules.isNotEmpty())
//
//        if (node.folderModules.isNotEmpty())  node.folderModules.astGroup("folders", tail = node.fileModules.isEmpty())
//        if (node.fileModules.isNotEmpty()) node.fileModules.astGroup("files", tail = true)
//    }

    override fun visit(node: InvalidNode) {
        base("[Invalid expression.]")
        node.children.astGroup("children", tail = false)
        node.errors.run {
            for (i in indices) {
                label(this[i].toString(), i == lastIndex)
            }
        }
    }

    override fun visit(node: IdentifierExpr) = base("reference ${node.name}")

    override fun visit(node: NullExpr) = base("null ref")

    override fun visit(node: ThisExpr) = base("this ref")

    override fun visit(node: IntExpr) = base("Int: ${node.value}")

    override fun visit(node: LongExpr) = base("Long: ${node.value}")

    override fun visit(node: FloatExpr) = base("Float: ${node.value}")

    override fun visit(node: DoubleExpr) = base("Double: ${node.value}")

    override fun visit(node: CharExpr) = base("Char: \'${node.value}\'")

    override fun visit(node: StringExpr) = base("String: \"${node.value}\"")

    override fun visit(node: BooleanExpr) = base(node.value.toString())

    override fun visit(node: MultiNode) {
        for (i in node.list.indices) {
            node.list[i].ast(indent, i == node.list.lastIndex)
        }
    }

    override fun visit(node: MultiExpr) {
        for (i in node.list.indices) {
            node.list[i].ast(indent, false)
        }
        node.last.ast(indent, true)
    }

//    override fun visit(node: DeclareModuleExpr) {
//        base("local module")
//
//        label("name: ${node.name}", false)
//
//        node.declarationsAST.let { d ->
//            d.modules.astGroup("submodules", tail = d.functions.isEmpty() && d.variables.isEmpty())
//            d.functions.astGroup("functions", tail = d.variables.isEmpty())
//            d.variables.astGroup("variables", tail = true)
//        }
//    }

    override fun visit(node: DeclareClassNode) {
        base("declare class")
        val modifiers = node.modifiers
        if (modifiers.isNotEmpty()) {
            label("modifiers: ${modifiers.joinToString { it.name.toLowerCase() }}", false)
        }
        label("name: ${node.name}", node.body.isEmpty() && node.implements.isEmpty() && node.parameters.isEmpty())
        val (names, values) = node.parameters.map { it.name to it.value }.unzip()
        if (names.isNotEmpty()) {
            label("parameters: ${names.joinToString(", ")}", tail = values.isNotEmpty() && node.body.isEmpty() && node.implements.isEmpty())
        }
        values.filterNotNull().astGroup("default values", tail = node.body.isEmpty() && node.implements.isEmpty())
        node.implements.astGroup("implements", tail = node.body.isEmpty())
        node.body.astGroup("body", tail = true)
    }

    override fun visit(node: DeclareEnumClassNode) {
        base("declare enum class")
        val modifiers = node.modifiers
        if (modifiers.isNotEmpty()) {
            label("modifiers: ${modifiers.joinToString { it.name.toLowerCase() }}", false)
        }
        val values = node.values
        label("name: ${node.name}", values.isEmpty() && node.body.isEmpty())
        if (values.isNotEmpty()) {
            label("values: ${values.joinToString(", ")}", tail = node.body.isEmpty())
        }
        node.body.astGroup("body", tail = true)
    }

    override fun visit(node: DeclareInterfaceNode) {
        base("declare interface")
        val modifiers = node.modifiers
        if (modifiers.isNotEmpty()) {
            label("modifiers: ${modifiers.joinToString { it.name.toLowerCase() }}", false)
        }
        label("name: ${node.name}", node.body.isEmpty() && node.implements.isEmpty())
        node.implements.astGroup("implements", tail = node.body.isEmpty())
        node.body.astGroup("body", tail = true)
    }

    override fun visit(node: DeclareObjectNode) {
        base("declare object")
        val modifiers = node.modifiers
        if (modifiers.isNotEmpty()) {
            label("modifiers: ${modifiers.joinToString { it.name.toLowerCase() }}", false)
        }
        label("name: ${node.name}", node.body.isEmpty() && node.implements.isEmpty())
        node.implements.astGroup("implements", tail = node.body.isEmpty())
        node.body.astGroup("body", tail = true)
    }

    override fun visit(node: DeclareFunctionNode) {
        base("declare function")
        val modifiers = node.modifiers
        if (modifiers.isNotEmpty()) {
            label("modifiers: ${modifiers.joinToString { it.name.toLowerCase() }}", false)
        }
        label("name: ${node.name}", false)
        node.function.astLabel("function", tail = true)
    }

    override fun visit(node: DeclareVariableNode) {
        base(if (node.mutable) "var" else "val")
        val modifiers = node.modifiers
        if (modifiers.isNotEmpty()) {
            label("modifiers: ${modifiers.joinToString { it.name.toLowerCase() }}", false)
        }
        label("name: ${node.name}", node.value == null)
        node.value?.astLabel("value", tail = true)
    }

    override fun visit(node: DelegatingVariableNode) {
        base(if (node.mutable) "var" else "val")
        val modifiers = node.modifiers
        if (modifiers.isNotEmpty()) {
            label("modifiers: ${modifiers.joinToString { it.name.toLowerCase() }}", tail = false)
        }
        label("name: ${node.name}", tail = false)
        node.delegate.astLabel("delegate", tail = true)
    }

    override fun visit(node: DestructuringVariableNode) {
        base(if (node.mutable) "var" else "val")
        val modifiers = node.modifiers
        if (modifiers.isNotEmpty()) {
            label("modifiers: ${modifiers.joinToString { it.name.toLowerCase() }}", tail = false)
        }
        label("destructuring: ${node.names.joinToString(", ", "(", ")")}", tail = false)
        node.value.astLabel("value", tail = true)
    }

    override fun visit(node: ObjectExpr) {
        base("object")
        node.implements.astGroup("implements", tail = node.body.isEmpty())
        node.body.astGroup("body", tail = true)
    }

    override fun visit(node: FunctionExpr) {
        base("function")
        val (names, values) = node.parameters.map { it.name to it.value }.unzip()
        if (names.isNotEmpty()) {
            label("parameters: ${names.joinToString(", ")}", tail = values.isNotEmpty() && node.body == null)
        }
        values.filterNotNull().astGroup("default values", tail = node.body == null)
        node.body?.astLabel("body", tail = true)
    }

    override fun visit(node: LambdaExpr) {
        base("lambda")
        val parameters = node.parameters
        if (parameters.isNotEmpty()) {
            label("parameters: ${parameters.joinToString(", ")}", tail = false)
        }
        node.body.astLabel("body", tail = true)
    }

    override fun visit(node: InitializerNode) {
        base("init")
        node.body.astLabel("body", tail = true)
    }

//    override fun visit(node: TypeAliasExpr) {
//        base("typealias")
//
//        label("name: ${node.name}", false)
//
//        if (node.generics.isNotEmpty()) {
//            label(node.generics.joinToString(prefix = "generics: <", postfix = ">"), false)
//        }
//
//        label("type: ${node.type}", true)
//    }

//    override fun visit(node: ExternalModuleExpr) {
//        base("external module")
//
//        label("name: ${node.name}", false)
//
//        node.declarationsAST.let { d ->
//            d.modules.astGroup("submodules", tail = d.functions.isEmpty() && d.variables.isEmpty())
//            d.functions.astGroup("functions", tail = d.variables.isEmpty())
//            d.variables.astGroup("variables", tail = true)
//        }
//    }

//    override fun visit(node: ExternalLetExpr) {
//        base("external let")
//
//        label("name: ${node.name}", true)
//    }

//    override fun visit(node: ExternalFunctionExpr) {
//        base("external function")
//
//        label("name: ${node.name}", true)
//
//        if (node.generics.isNotEmpty()) {
//            label(node.generics.joinToString(prefix = "generics: <", postfix = ">"), false)
//        }
//
//        label(node.arguments.joinToString(prefix = "arguments: (", postfix = ")"), false)
//        label("return: ${node.returnType}", true)
//    }

    override fun visit(node: AssignNode) {
        base("assign ${node.name}")

        node.value.ast(tail = true)
    }

//    override fun visit(node: ClosureExpr) {
//        base("closure")
//
//        label(node.arguments.joinToString(prefix = "arguments: (", postfix = ")"), false)
//        node.body.astLabel("body", tail = true)
//    }

    override fun visit(node: UnitExpr) {
        base("unit")
    }

//    override fun visit(node: TupleExpr) {
//        base("tuple")
//
//        for (i in 0 until node.list.size) {
//            node.list[i].ast(tail = i == node.list.lastIndex)
//        }
//    }

//    override fun visit(node: ListLiteralExpr) {
//        base("list literal")
//
//        for (i in 0 until node.list.size) {
//            node.list[i].ast(tail = i == node.list.lastIndex)
//        }
//    }

//    override fun visit(node: MapLiteralExpr) {
//        base("map literal")
//
//        for ((i, entry) in node.map.entries.withIndex()) {
//            listOf(entry.key, entry.value).astGroup("entry $i", tail = i == node.map.size - 1)
//        }
//    }

//    override fun visit(node: TemplateExpr) {
//        base("template")
//
//        node.target.astLabel("target", tail = false)
//        label(node.typeArguments.joinToString(prefix = "type arguments: <", postfix = ">"), true)
//    }

    override fun visit(node: InvokeExpr) {
        base("invoke")

        node.target.astLabel("target", tail = node.arguments.isEmpty())
        node.arguments.astGroup("arguments", tail = true)
    }

    override fun visit(node: InvokeLocalExpr) {
        base("invoke local")

        label("name: ${node.name}", tail = node.arguments.isEmpty())
        node.arguments.astGroup("arguments", tail = true)
    }

    override fun visit(node: InvokeMemberExpr) {
        base("invoke member function")

        node.target.astLabel("target", tail = false)
        label("null safe: ${node.nullSafe}", tail = false)
        label("name: ${node.name}", node.arguments.isEmpty())
        node.arguments.astGroup("arguments", tail = true)
    }

    override fun visit(node: UnaryOperation) {
        base("unary ${node.operator}")

        node.target.ast(tail = true)
    }

    override fun visit(node: PrefixAssignUnaryOperation) {
        base("pre-assign unary ${node.operator}")

        node.target.ast(tail = true)
    }

    override fun visit(node: PostfixAssignUnaryOperation) {
        base("post-assign unary ${node.operator}")

        node.target.ast(tail = true)
    }

    override fun visit(node: BinaryOperation) {
        base("binary ${node.operator}")

        node.left.ast(tail = false)
        node.right.ast(tail = true)
    }

    override fun visit(node: AssignOperation) {
        base("assign operation ${node.operator}")

        node.left.ast(tail = false)
        node.right.ast(tail = true)
    }

    override fun visit(node: ReturnExpr) {
        base("return")

        node.value.ast(tail = true)
    }

    override fun visit(node: ThrowExpr) {
        base("throw")

        node.value.ast(tail = true)
    }

    override fun visit(node: NotNullExpr) {
        base("not null")

        node.value.ast(tail = true)
    }

    override fun visit(node: IfNode) {
        base("if expr")

        node.condition.astLabel("condition", tail = false)
        node.thenBranch.astLabel("then", tail = node.elseBranch == null)

        if (node.elseBranch != null) {
            node.elseBranch.astLabel("else", tail = true)
        }
    }

    override fun visit(node: IfExpr) {
        base("if node")

        node.condition.astLabel("condition", tail = false)
        node.thenBranch.astLabel("then", tail = false)
        node.elseBranch.astLabel("else", tail = true)
    }

    override fun visit(node: DoWhileNode) {
        base("do-while")

        node.body.astLabel("body", tail = false)
        node.condition.astLabel("condition", tail = true)
    }

    override fun visit(node: WhileNode) {
        base("while")

        node.condition.astLabel("condition", tail = false)
        node.body.astLabel("body", tail = true)
    }

    override fun visit(node: BreakExpr) = base("break")

    override fun visit(node: ContinueExpr) = base("continue")

    override fun visit(node: ForNode) {
        base("for")

        label("variable name: ${node.variableName}", tail = false)
        node.iterable.astLabel("iterable", tail = false)
        node.body.astLabel("body", tail = true)
    }

    override fun visit(node: SubscriptAccessExpr) {
        base("subscript access")

        node.target.astLabel("target", tail = false)
        node.arguments.astGroup("arguments", tail = true)
    }

    override fun visit(node: SubscriptAssignNode) {
        base("subscript assign")

        node.target.astLabel("target", tail = false)
        node.arguments.astGroup("arguments", tail = false)
        node.value.astLabel("value", tail = true)
    }

//    override fun visit(node: TupleIndexAccessExpr) {
//        base("tuple index access")
//
//        node.target.astLabel("target", tail = false)
//        label("index: ${node.index}", true)
//    }

    override fun visit(node: PropertyAccessExpr) {
        base("property access")

        node.target.astLabel("target", tail = false)
        label("null safe: ${node.nullSafe}", tail = false)
        label("name: ${node.name}", true)
    }

    override fun visit(node: PropertyAssignNode) {
        base("property access")

        node.target.astLabel("target", tail = false)
        label("null safe: ${node.nullSafe}", tail = false)
        label("name: ${node.name}", true)
        node.value.astLabel("value", tail = true)
    }
}