package io.github.cafeteriaguild.lin.parser

import net.notjustanna.tartar.createParser
import net.notjustanna.tartar.extensions.ensureEOF
import io.github.cafeteriaguild.lin.ast.expr.Expr
import io.github.cafeteriaguild.lin.ast.expr.misc.MultiExpr
import io.github.cafeteriaguild.lin.lexer.TokenType
import io.github.cafeteriaguild.lin.parser.utils.matchAll

val linStdParser = createParser(linStdGrammar) {
    ensureEOF {
        val start = peek()
        val list = mutableListOf<Expr>()
        matchAll(TokenType.NL, TokenType.SEMICOLON)
        do {
            list += parseExpression()
        } while (matchAll(TokenType.NL, TokenType.SEMICOLON))
        MultiExpr(list, start.section)
    }
}