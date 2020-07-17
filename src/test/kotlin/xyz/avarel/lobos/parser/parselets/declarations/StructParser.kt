package xyz.avarel.lobos.parser.parselets.declarations

import xyz.avarel.lobos.ast.expr.Expr
import xyz.avarel.lobos.ast.types.ArgumentParameterAST
import xyz.avarel.lobos.lexer.Token
import xyz.avarel.lobos.lexer.TokenType
import xyz.avarel.lobos.parser.*

/*
struct A<T>: G<T> {
    mut x: T
}
 */

object StructParser : PrefixParser {
    override fun parse(parser: Parser, modifiers: List<Modifier>, token: Token): Expr {
        val ident = parser.eat(TokenType.IDENT)
        val name = ident.string

        val generics = parser.parseGenericParameters()

        val parentType = if (parser.match(TokenType.COLON)) {
            parser.parseTypeAST()
        } else {
            null
        }

        val members = mutableListOf<ArgumentParameterAST>()

        if (parser.match(TokenType.L_BRACE)) {

            parser.eat(TokenType.R_BRACE)
        }

        TODO()
    }
}