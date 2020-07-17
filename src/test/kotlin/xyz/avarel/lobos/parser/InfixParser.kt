package xyz.avarel.lobos.parser

import xyz.avarel.lobos.ast.expr.Expr
import xyz.avarel.lobos.lexer.Token

interface InfixParser {
    val precedence: Int
    fun parse(parser: Parser, token: Token, left: Expr): Expr
}