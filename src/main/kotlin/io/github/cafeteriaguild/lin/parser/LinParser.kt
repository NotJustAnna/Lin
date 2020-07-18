package io.github.cafeteriaguild.lin.parser

import net.notjustanna.tartar.api.parser.SyntaxException
import net.notjustanna.tartar.createParser
import io.github.cafeteriaguild.lin.ast.expr.Expr
import io.github.cafeteriaguild.lin.ast.expr.Node
import io.github.cafeteriaguild.lin.ast.expr.misc.InvalidExpr
import io.github.cafeteriaguild.lin.ast.expr.misc.MultiExpr
import io.github.cafeteriaguild.lin.ast.expr.misc.MultiNode
import io.github.cafeteriaguild.lin.lexer.TokenType
import io.github.cafeteriaguild.lin.parser.utils.matchAll

@OptIn(ExperimentalStdlibApi::class)
val linStdParser = createParser(linStdGrammar) {
    val start = peek()
    val list = mutableListOf<Expr>()
    matchAll(TokenType.NL, TokenType.SEMICOLON)
    val expr = try {
        do {
            list += parseExpression()
        } while (matchAll(TokenType.NL, TokenType.SEMICOLON) && !eof)

        if (list.isNotEmpty() && list.last() is Node) {
            val last = list.removeLast() as Node
            MultiNode(list, last, start.section)
        } else
            MultiExpr(list, start.section)

    } catch (e: Exception) {
        InvalidExpr {
            section(start.section)
            child(*list.toTypedArray())
            error(e)
        }
    }

    if (!eof) InvalidExpr {
        child(expr)
        error(SyntaxException("Should've reached end of content", eat().section))
    }
    else expr
}