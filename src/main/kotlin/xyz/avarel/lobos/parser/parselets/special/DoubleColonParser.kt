package xyz.avarel.lobos.parser.parselets.special

import xyz.avarel.lobos.ast.expr.Expr
import xyz.avarel.lobos.ast.expr.misc.TemplateExpr
import xyz.avarel.lobos.ast.types.TypeAST
import xyz.avarel.lobos.lexer.Token
import xyz.avarel.lobos.lexer.TokenType
import xyz.avarel.lobos.parser.InfixParser
import xyz.avarel.lobos.parser.Parser
import xyz.avarel.lobos.parser.Precedence
import xyz.avarel.lobos.parser.parseTypeAST

object DoubleColonParser : InfixParser {
    override val precedence: Int get() = Precedence.POSTFIX

    override fun parse(parser: Parser, token: Token, left: Expr): Expr {
        parser.eat(TokenType.LT)
        val typeArguments = mutableListOf<TypeAST>()
        do {
            typeArguments += parser.parseTypeAST()
        } while (parser.match(TokenType.COMMA))
        val gt = parser.eat(TokenType.GT)

        return TemplateExpr(left, typeArguments, left.span(gt))
    }
}