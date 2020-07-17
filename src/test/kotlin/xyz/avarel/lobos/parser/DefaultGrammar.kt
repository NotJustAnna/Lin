package xyz.avarel.lobos.parser

import xyz.avarel.lobos.ast.expr.ops.BinaryOperationType
import xyz.avarel.lobos.ast.expr.ops.UnaryOperationType
import xyz.avarel.lobos.lexer.TokenType
import xyz.avarel.lobos.parser.parselets.BinaryOperatorParser
import xyz.avarel.lobos.parser.parselets.UnaryOperatorParser
import xyz.avarel.lobos.parser.parselets.declarations.*
import xyz.avarel.lobos.parser.parselets.nodes.*
import xyz.avarel.lobos.parser.parselets.special.*

object DefaultGrammar : Grammar(hashMapOf(), hashMapOf()) {
    init {
        prefix(TokenType.INT, IntParser)
        prefix(TokenType.DECIMAL, DecimalParser)

        prefix(TokenType.USE, UseParser)
        prefix(TokenType.MOD, ModuleParser)
        prefix(TokenType.STRING, StringParser)
        prefix(TokenType.IDENT, IdentParser)
        prefix(TokenType.TRUE, BooleanParser(true))
        prefix(TokenType.FALSE, BooleanParser(false))
        prefix(TokenType.RETURN, ReturnParser)
        prefix(TokenType.IF, IfParser)
        prefix(TokenType.WHILE, WhileParser)
        prefix(TokenType.NULL, NullParser)

        prefix(TokenType.L_PAREN, ParenParser)
        prefix(TokenType.L_BRACKET, CollectionParser)

        prefix(TokenType.LET, LetParser)
        prefix(TokenType.TYPE, TypeAliasParser)
        prefix(TokenType.DEF, FunctionParser)
        prefix(TokenType.EXTERNAL, ExternalParser)

        prefix(TokenType.PIPE, ClosureParser)
        prefix(TokenType.OR, ClosureParser)

        prefix(TokenType.BANG, UnaryOperatorParser(UnaryOperationType.NOT))
        prefix(TokenType.PLUS, UnaryOperatorParser(UnaryOperationType.POSITIVE))
        prefix(TokenType.MINUS, UnaryOperatorParser(UnaryOperationType.NEGATIVE))

        infix(TokenType.L_BRACKET, SubscriptParser)
        infix(TokenType.DOT, DotParser)
        infix(TokenType.DOUBLE_COLON, DoubleColonParser)
        infix(TokenType.L_PAREN, InvocationParser)
        infix(TokenType.EQ, BinaryOperatorParser(Precedence.EQUALITY, BinaryOperationType.EQUALS))
        infix(TokenType.NEQ, BinaryOperatorParser(Precedence.EQUALITY, BinaryOperationType.NOT_EQUALS))
        infix(TokenType.PLUS, BinaryOperatorParser(Precedence.ADDITIVE, BinaryOperationType.ADD))
        infix(TokenType.MINUS, BinaryOperatorParser(Precedence.ADDITIVE, BinaryOperationType.SUBTRACT))
        infix(TokenType.ASTERISK, BinaryOperatorParser(Precedence.MULTIPLICATIVE, BinaryOperationType.MULTIPLY))
        infix(TokenType.F_SLASH, BinaryOperatorParser(Precedence.MULTIPLICATIVE, BinaryOperationType.DIVIDE))

        infix(TokenType.AND, BinaryOperatorParser(Precedence.CONJUNCTION, BinaryOperationType.AND))
        infix(TokenType.OR, BinaryOperatorParser(Precedence.DISJUNCTION, BinaryOperationType.OR))
    }

    fun prefix(type: TokenType, parselet: PrefixParser) {
        if (type in prefixParsers) {
            throw IllegalStateException("INTERNAL: attempted to override existing $type parselet")
        }
        prefixParsers[type] = parselet
    }

    fun infix(type: TokenType, parselet: InfixParser) {
        if (type in infixParsers) {
            throw IllegalStateException("INTERNAL: attempted to override existing $type parselet")
        }
        infixParsers[type] = parselet
    }
}