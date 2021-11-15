package net.notjustanna.lin.parser

import net.notjustanna.lin.ast.node.*
import net.notjustanna.lin.ast.node.declare.DeclareFunctionExpr
import net.notjustanna.lin.ast.node.value.FunctionExpr
import net.notjustanna.lin.ast.node.value.NullExpr
import net.notjustanna.lin.lexer.LinToken
import net.notjustanna.lin.lexer.TokenType
import net.notjustanna.lin.parser.utils.matchAll
import net.notjustanna.tartar.api.grammar.Grammar
import net.notjustanna.tartar.api.parser.Parser
import net.notjustanna.tartar.api.parser.SourceParser
import net.notjustanna.tartar.api.parser.SyntaxException

public typealias LinSourceParser = SourceParser<TokenType, LinToken, Node, Node>

internal fun linStdParser(grammar: Grammar<TokenType, LinToken, Node>) = Parser.create(grammar) {
    if (this.eof) {
        return@create NullExpr()
    }
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
    if (!eof && expr !is InvalidNode) InvalidNode {
        child(expr)
        error(SyntaxException("Should've reached end of content", eat().section))
    } else expr
}
