package xyz.avarel.lobos.parser.parselets.declarations

import xyz.avarel.lobos.ast.expr.Expr
import xyz.avarel.lobos.ast.expr.declarations.DeclareLetExpr
import xyz.avarel.lobos.ast.expr.declarations.ExternalLetExpr
import xyz.avarel.lobos.ast.types.TypeAST
import xyz.avarel.lobos.lexer.Token
import xyz.avarel.lobos.lexer.TokenType
import xyz.avarel.lobos.parser.*

object LetParser : PrefixParser {
    override fun parse(parser: Parser, modifiers: List<Modifier>, token: Token): Expr {
        if (Modifier.EXTERNAL in modifiers) {
            val isMutable = parser.match(TokenType.MUT)
            val ident = parser.eat(TokenType.IDENT)
            val name = ident.string

            parser.eat(TokenType.COLON)
            val type: TypeAST = parser.parseTypeAST()
            return ExternalLetExpr(isMutable, name, type, token.span(type))
        }

        val pattern = parser.parsePattern()

        parser.eat(TokenType.ASSIGN)

        val expr = parser.parseExpr()
        return DeclareLetExpr(pattern, expr, token.span(expr))
    }
}