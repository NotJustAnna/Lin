package io.github.cafeteriaguild.lin.parser

import net.notjustanna.tartar.createGrammar
import io.github.cafeteriaguild.lin.ast.expr.Expr
import io.github.cafeteriaguild.lin.ast.expr.ops.BinaryOperationType
import io.github.cafeteriaguild.lin.ast.expr.ops.UnaryAssignOperationType
import io.github.cafeteriaguild.lin.ast.expr.ops.UnaryOperationType
import io.github.cafeteriaguild.lin.lexer.TokenType
import io.github.cafeteriaguild.lin.lexer.TokenType.*
import io.github.cafeteriaguild.lin.parser.parselets.BinaryOperatorParser
import io.github.cafeteriaguild.lin.parser.parselets.PostAssignUnaryOperatorParser
import io.github.cafeteriaguild.lin.parser.parselets.PreAssignUnaryOperatorParser
import io.github.cafeteriaguild.lin.parser.parselets.UnaryOperatorParser
import io.github.cafeteriaguild.lin.parser.parselets.declarations.DeclareVariableParser
import io.github.cafeteriaguild.lin.parser.parselets.declarations.FunctionParser
import io.github.cafeteriaguild.lin.parser.parselets.nodes.*
import io.github.cafeteriaguild.lin.parser.parselets.special.*

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
    infix(REM, BinaryOperatorParser(Precedence.MULTIPLICATIVE, BinaryOperationType.REMAINING))
    infix(AND, BinaryOperatorParser(Precedence.CONJUNCTION, BinaryOperationType.AND))
    infix(OR, BinaryOperatorParser(Precedence.DISJUNCTION, BinaryOperationType.OR))
    infix(LT, BinaryOperatorParser(Precedence.COMPARISON, BinaryOperationType.LT))
    infix(LTE, BinaryOperatorParser(Precedence.COMPARISON, BinaryOperationType.LTE))
    infix(GT, BinaryOperatorParser(Precedence.COMPARISON, BinaryOperationType.GT))
    infix(GTE, BinaryOperatorParser(Precedence.COMPARISON, BinaryOperationType.GTE))
    infix(ELVIS, BinaryOperatorParser(Precedence.ELVIS, BinaryOperationType.ELVIS))
    infix(IN, BinaryOperatorParser(Precedence.NAMED_CHECKS, BinaryOperationType.IN))
    infix(RANGE, BinaryOperatorParser(Precedence.RANGE, BinaryOperationType.RANGE))

    // Increment/decrement
    prefix(DECREMENT, PreAssignUnaryOperatorParser(UnaryAssignOperationType.DECREMENT))
    prefix(INCREMENT, PreAssignUnaryOperatorParser(UnaryAssignOperationType.INCREMENT))
    infix(DECREMENT, PostAssignUnaryOperatorParser(UnaryAssignOperationType.DECREMENT))
    infix(INCREMENT, PostAssignUnaryOperatorParser(UnaryAssignOperationType.INCREMENT))

    prefix(VAL, DeclareVariableParser(false))
    prefix(VAR, DeclareVariableParser(true))
    prefix(IDENTIFIER, IdentifierParser)
    infix(L_PAREN, InvocationParser)
    infix(L_BRACKET, SubscriptParser)
    infix(DOT, DotParser(false))
    infix(QUESTION_DOT, DotParser(true))
    infix(DOUBLE_BANG, DoubleBangParser)
    infix(BANG, InfixBangParser)
    infix(IDENTIFIER, InfixInvocationParser)

    prefix(RETURN, ReturnParser)
    prefix(THROW, ThrowParser)
    prefix(IF, IfParser)
    prefix(DO, DoWhileParser)
    prefix(WHILE, WhileParser)
    prefix(FOR, ForParser)
    prefix(FUN, FunctionParser)
}