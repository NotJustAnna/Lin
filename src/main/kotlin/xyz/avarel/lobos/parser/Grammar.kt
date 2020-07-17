package xyz.avarel.lobos.parser

import xyz.avarel.lobos.lexer.TokenType

open class Grammar(
    val prefixParsers: MutableMap<TokenType, PrefixParser>,
    val infixParsers: MutableMap<TokenType, InfixParser>
)