package io.github.cafeteriaguild.lin.parser

import net.notjustanna.tartar.api.parser.SyntaxException
import net.notjustanna.tartar.createGrammar
import io.github.cafeteriaguild.lin.ast.expr.Expr
import io.github.cafeteriaguild.lin.ast.expr.access.AssignExpr
import io.github.cafeteriaguild.lin.ast.expr.access.PropertyAccessExpr
import io.github.cafeteriaguild.lin.ast.expr.access.PropertyAssignExpr
import io.github.cafeteriaguild.lin.ast.expr.invoke.InvokeExpr
import io.github.cafeteriaguild.lin.ast.expr.invoke.InvokeLocalExpr
import io.github.cafeteriaguild.lin.ast.expr.invoke.InvokeMemberExpr
import io.github.cafeteriaguild.lin.ast.expr.misc.UnitExpr
import io.github.cafeteriaguild.lin.ast.expr.nodes.IdentExpr
import io.github.cafeteriaguild.lin.ast.expr.nodes.ReturnExpr
import io.github.cafeteriaguild.lin.ast.expr.ops.BinaryOperationType
import io.github.cafeteriaguild.lin.ast.expr.ops.UnaryOperationType
import io.github.cafeteriaguild.lin.lexer.TokenType
import io.github.cafeteriaguild.lin.lexer.TokenType.*
import io.github.cafeteriaguild.lin.parser.grammar.BinaryOperatorParser
import io.github.cafeteriaguild.lin.parser.grammar.UnaryOperatorParser
import io.github.cafeteriaguild.lin.parser.grammar.nodes.*

val linStdGrammar = createGrammar<TokenType, Expr> {
    // Nodes
    prefix(INT, IntParser)
    prefix(LONG, LongParser)
    prefix(FLOAT, FloatParser)
    prefix(DOUBLE, DoubleParser)
    prefix(NULL, NullParser)
    prefix(CHAR, CharParser)
    prefix(STRING, StringParser)
    prefix(TRUE, BooleanParser(true))
    prefix(FALSE, BooleanParser(false))

    // Unary operations
    prefix(BANG, UnaryOperatorParser(UnaryOperationType.NOT))
    prefix(PLUS, UnaryOperatorParser(UnaryOperationType.POSITIVE))
    prefix(MINUS, UnaryOperatorParser(UnaryOperationType.NEGATIVE))

    // Binary operations
    infix(EQ, BinaryOperatorParser(Precedence.EQUALITY, BinaryOperationType.EQUALS))
    infix(NEQ, BinaryOperatorParser(Precedence.EQUALITY, BinaryOperationType.NOT_EQUALS))
    infix(PLUS, BinaryOperatorParser(Precedence.ADDITIVE, BinaryOperationType.ADD))
    infix(MINUS, BinaryOperatorParser(Precedence.ADDITIVE, BinaryOperationType.SUBTRACT))
    infix(ASTERISK, BinaryOperatorParser(Precedence.MULTIPLICATIVE, BinaryOperationType.MULTIPLY))
    infix(SLASH, BinaryOperatorParser(Precedence.MULTIPLICATIVE, BinaryOperationType.DIVIDE))
    infix(AND, BinaryOperatorParser(Precedence.CONJUNCTION, BinaryOperationType.AND))
    infix(OR, BinaryOperatorParser(Precedence.DISJUNCTION, BinaryOperationType.OR))
    prefix(IDENTIFIER) {
        if (match(ASSIGN)) {
            AssignExpr(
                it.value,
                parseExpression(),
                it.section
            )
        } else {
            IdentExpr(it.value, it.section)
        }
    }
    prefix(RETURN) {
        val expr = if (!eof && !matchAny(NL, SEMICOLON)) {
            parseExpression()
        } else UnitExpr(it.section)

        ReturnExpr(expr, it.span(expr))
    }

    infix(L_PAREN, Precedence.POSTFIX) { left, _ ->
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
    infix(DOT, Precedence.POSTFIX) { left, _ ->
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