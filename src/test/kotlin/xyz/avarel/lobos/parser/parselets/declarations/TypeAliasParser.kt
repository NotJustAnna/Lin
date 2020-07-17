package xyz.avarel.lobos.parser.parselets.declarations

import xyz.avarel.lobos.ast.expr.Expr
import xyz.avarel.lobos.ast.expr.declarations.TypeAliasExpr
import xyz.avarel.lobos.lexer.Token
import xyz.avarel.lobos.lexer.TokenType
import xyz.avarel.lobos.parser.*

object TypeAliasParser : PrefixParser {
    override fun parse(parser: Parser, modifiers: List<Modifier>, token: Token): Expr {
        val ident = parser.eat(TokenType.IDENT)
        val name = ident.string

        val generics = parser.parseGenericParameters()

        parser.eat(TokenType.ASSIGN)

        val type = parser.parseTypeAST()

        return TypeAliasExpr(name, generics, type, token.span(type))
    }
}