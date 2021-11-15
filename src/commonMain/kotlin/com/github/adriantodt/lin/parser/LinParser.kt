package com.github.adriantodt.lin.parser

import com.github.adriantodt.lin.ast.node.*
import com.github.adriantodt.lin.ast.node.declare.DeclareFunctionExpr
import com.github.adriantodt.lin.ast.node.value.FunctionExpr
import com.github.adriantodt.lin.ast.node.value.NullExpr
import com.github.adriantodt.lin.lexer.LinToken
import com.github.adriantodt.lin.lexer.TokenType
import com.github.adriantodt.lin.parser.utils.matchAll
import com.github.adriantodt.tartar.api.grammar.Grammar
import com.github.adriantodt.tartar.api.parser.Parser
import com.github.adriantodt.tartar.api.parser.SourceParser
import com.github.adriantodt.tartar.api.parser.SyntaxException

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
