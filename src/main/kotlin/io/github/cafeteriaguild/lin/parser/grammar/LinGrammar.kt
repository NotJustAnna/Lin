package io.github.cafeteriaguild.lin.parser.grammar

import net.notjustanna.tartar.api.parser.SyntaxException
import net.notjustanna.tartar.createGrammar
import io.github.cafeteriaguild.lin.ast.expr.Expr
import io.github.cafeteriaguild.lin.ast.expr.access.PropertyAccessExpr
import io.github.cafeteriaguild.lin.ast.expr.access.PropertyAssignExpr
import io.github.cafeteriaguild.lin.ast.expr.invoke.InvokeExpr
import io.github.cafeteriaguild.lin.ast.expr.invoke.InvokeLocalExpr
import io.github.cafeteriaguild.lin.ast.expr.invoke.InvokeMemberExpr
import io.github.cafeteriaguild.lin.ast.expr.nodes.IdentExpr
import io.github.cafeteriaguild.lin.lexer.TokenType
import io.github.cafeteriaguild.lin.lexer.TokenType.*
import io.github.cafeteriaguild.lin.parser.Precedence

val linStdGrammar = createGrammar<TokenType, Expr> {
    nodeParselets()

    infix(L_PAREN, Precedence.DOT) { left, _ ->
        val arguments = mutableListOf<Expr>()

        if (!match(R_PAREN)) {
            do {
                arguments += parseExpression()
            } while (match(COMMA))
            eat(R_PAREN)
        }

        val rParen = last

        val position = left.span(rParen)

        when (left) {
            is PropertyAccessExpr -> InvokeMemberExpr(left.target, left.name, arguments, position)
            is IdentExpr -> InvokeLocalExpr(left.name, arguments, position)
            else -> InvokeExpr(left, arguments, position)
        }
    }
    infix(DOT, Precedence.DOT) { left, _ ->
        val ident = eat()
        when (ident.type) {
            //INT -> {
            //    val index = ident.value.toInt()
            //    TupleIndexAccessExpr(left, index, left.span(ident))
            //}
            IDENTIFIER -> {
                val name = ident.value
                if (match(ASSIGN)) {
                    val value = parseExpression()
                    PropertyAssignExpr(left, name, value, left.span(value))
                } else {
                    PropertyAccessExpr(left, name, left.span(ident))
                }
            }
            else -> throw SyntaxException("Invalid identifier", ident.section)
        }
    }
}