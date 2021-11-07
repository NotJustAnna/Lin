package net.notjustanna.lin.parser

import net.notjustanna.lin.ast.node.*
import net.notjustanna.lin.ast.node.declare.DeclareFunctionExpr
import net.notjustanna.lin.ast.node.value.FunctionExpr
import net.notjustanna.lin.lexer.TokenType
import net.notjustanna.lin.parser.utils.matchAll
import net.notjustanna.tartar.api.grammar.Grammar
import net.notjustanna.tartar.api.parser.Parser
import net.notjustanna.tartar.api.parser.SyntaxException

internal fun linStdParser(grammar: Grammar<TokenType, Node>) = Parser.create(grammar) {
    val start = peek()
    val list = mutableListOf<Node>()
    matchAll(TokenType.NL, TokenType.SEMICOLON)
    val expr = try {
        do {
            val expr = parseExpression()

            list += when {
                expr is FunctionExpr && expr.name != null -> DeclareFunctionExpr(expr.name, expr, expr.section)
                else -> expr
            }
        } while (matchAll(TokenType.NL, TokenType.SEMICOLON) && !eof)

        if (list.isNotEmpty() && list.last() is Expr) {
            val last = list.removeLast() as Expr
            MultiExpr(list, last, start.section)
        } else
            MultiNode(list, start.section)

    } catch (e: Exception) {
        InvalidNode {
            section(start.section)
            child(*list.toTypedArray())
            error(e)
        }
    }
    if (!eof) InvalidNode {
        child(expr)
        error(SyntaxException("Should've reached end of content", eat().section))
    } else expr
}
