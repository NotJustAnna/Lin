package com.github.adriantodt.lin.parser

import com.github.adriantodt.lin.ast.BinaryOperationType
import com.github.adriantodt.lin.ast.Node
import com.github.adriantodt.lin.ast.UnaryOperationType
import com.github.adriantodt.lin.lexer.TokenType
import com.github.adriantodt.lin.lexer.TokenType.*
import com.github.adriantodt.lin.parser.parselets.control.*
import com.github.adriantodt.lin.parser.parselets.misc.*
import com.github.adriantodt.lin.parser.parselets.operations.BinaryOperatorParser
import com.github.adriantodt.lin.parser.parselets.operations.UnaryOperatorParser
import com.github.adriantodt.lin.parser.parselets.value.*
import com.github.adriantodt.tartar.createGrammar

val linStdGrammar = createGrammar<TokenType, Node> {
    // Simple Expressions
    prefix(INT, IntParser)
    prefix(LONG, LongParser)
    prefix(FLOAT, FloatParser)
    prefix(DOUBLE, DoubleParser)
    prefix(NULL, NullParser)
    prefix(UNIT, UnitParser)
    prefix(THIS, ThisParser)
    prefix(CHAR, CharParser)
    prefix(STRING, StringParser)
    prefix(TRUE, BooleanParser(true))
    prefix(FALSE, BooleanParser(false))

    // Objects, Arrays and Functions
    prefix(L_BRACE, ObjectParser)
    prefix(L_BRACKET, ArrayParser)
    prefix(FUN, FunctionParser)

    // Control expressions
    prefix(RETURN, ReturnParser)
    prefix(THROW, ThrowParser)
    prefix(BREAK, BreakParser)
    prefix(CONTINUE, ContinueParser)

    // Conditional and Loop nodes
    prefix(IF, IfParser)
    prefix(DO, DoParser)
    prefix(WHILE, WhileParser)
    prefix(FOR, ForParser)

    // Unary operations
    prefix(BANG, UnaryOperatorParser(UnaryOperationType.NOT))
    prefix(DOUBLE_BANG, UnaryOperatorParser(UnaryOperationType.TRUTH))
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
    infix(IS, BinaryOperatorParser(Precedence.NAMED_CHECKS, BinaryOperationType.IS))

    //TODO Assign operations

    //TODO Prefix Increment/decrement

    // Miscellaneous parsers
    prefix(VAL, DeclareVariableParser(false))
    prefix(VAR, DeclareVariableParser(true))
    prefix(IDENTIFIER, IdentifierParser)
    prefix(L_PAREN, ParenthesisParser)
    prefix(TRY, TryParser)
    prefix(TYPEOF, TypeofParser)

    infix(DOT, DotParser(false))
    infix(QUESTION_DOT, DotParser(true))
    infix(BANG, InfixBangParser)
    infix(DOUBLE_BANG, DoubleBangParser)
    infix(L_PAREN, InvocationParser)
    infix(L_BRACKET, SubscriptParser)
}
