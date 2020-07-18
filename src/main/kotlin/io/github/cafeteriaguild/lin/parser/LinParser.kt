package io.github.cafeteriaguild.lin.parser

import net.notjustanna.tartar.createParser
import io.github.cafeteriaguild.lin.ast.expr.Expr
import io.github.cafeteriaguild.lin.ast.expr.misc.MultiExpr
import io.github.cafeteriaguild.lin.lexer.TokenType

val linStdParser = createParser(linStdGrammar) {
    val list = mutableListOf<Expr>()
    while (!eof) {
        list += parseExpression()
        match(TokenType.NL)
    }
    MultiExpr(list)
}