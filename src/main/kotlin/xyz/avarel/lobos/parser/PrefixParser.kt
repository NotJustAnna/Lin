package xyz.avarel.lobos.parser

import xyz.avarel.lobos.ast.expr.Expr
import xyz.avarel.lobos.lexer.Token

interface PrefixParser {
    fun parse(parser: Parser, modifiers: List<Modifier>, token: Token): Expr
}