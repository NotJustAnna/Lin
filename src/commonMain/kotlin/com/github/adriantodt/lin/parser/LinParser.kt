package com.github.adriantodt.lin.parser

import com.github.adriantodt.lin.ast.node.*
import com.github.adriantodt.lin.grammar.linStdGrammar
import com.github.adriantodt.lin.lexer.TokenType
import com.github.adriantodt.tartar.api.parser.Grammar
import com.github.adriantodt.tartar.api.parser.SyntaxException
import com.github.adriantodt.tartar.createParser
import io.github.cafeteriaguild.lin.parser.utils.matchAll

private fun createLinParser(grammar: Grammar<TokenType, Node>) = createParser(grammar) {
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

val linStdParser = createLinParser(linStdGrammar)
