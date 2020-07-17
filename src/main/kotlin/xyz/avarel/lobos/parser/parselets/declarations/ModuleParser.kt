package xyz.avarel.lobos.parser.parselets.declarations

import xyz.avarel.lobos.ast.expr.Expr
import xyz.avarel.lobos.ast.expr.declarations.DeclareModuleExpr
import xyz.avarel.lobos.ast.expr.declarations.ExternalModuleExpr
import xyz.avarel.lobos.lexer.Token
import xyz.avarel.lobos.lexer.TokenType
import xyz.avarel.lobos.parser.*

object ModuleParser : PrefixParser {
    override fun parse(parser: Parser, modifiers: List<Modifier>, token: Token): Expr {
        val name = parser.eat(TokenType.IDENT).string

        if (Modifier.EXTERNAL in modifiers) {
            val declarations = parser.parseExternalDeclarations()
            return ExternalModuleExpr(name, declarations, token.section)
        }

        val declarations = parser.parseDeclarations()

        return DeclareModuleExpr(name, declarations, token.section)
    }
}