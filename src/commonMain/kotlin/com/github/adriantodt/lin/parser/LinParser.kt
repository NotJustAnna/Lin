package com.github.adriantodt.lin.parser

import com.github.adriantodt.lin.ast.node.*
import com.github.adriantodt.lin.lexer.TokenType
import com.github.adriantodt.lin.parser.utils.matchAll
import com.github.adriantodt.tartar.api.grammar.Grammar
import com.github.adriantodt.tartar.api.parser.Parser
import com.github.adriantodt.tartar.api.parser.SyntaxException

internal fun linStdParser(grammar: Grammar<TokenType, Node>) = Parser.create(grammar) {
    val start = peek()
    val list = mutableListOf<Node>()
    matchAll(TokenType.NL, TokenType.SEMICOLON)
    val expr = try {
        do {
            list += parseExpression()
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
