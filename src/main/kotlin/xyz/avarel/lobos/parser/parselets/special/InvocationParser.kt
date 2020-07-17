package xyz.avarel.lobos.parser.parselets.special

import xyz.avarel.lobos.ast.expr.Expr
import xyz.avarel.lobos.ast.expr.access.PropertyAccessExpr
import xyz.avarel.lobos.ast.expr.invoke.InvokeExpr
import xyz.avarel.lobos.ast.expr.invoke.InvokeLocalExpr
import xyz.avarel.lobos.ast.expr.invoke.InvokeMemberExpr
import xyz.avarel.lobos.ast.expr.nodes.IdentExpr
import xyz.avarel.lobos.lexer.Token
import xyz.avarel.lobos.lexer.TokenType
import xyz.avarel.lobos.parser.InfixParser
import xyz.avarel.lobos.parser.Parser
import xyz.avarel.lobos.parser.Precedence

object InvocationParser : InfixParser {
    override val precedence: Int = Precedence.POSTFIX

    override fun parse(parser: Parser, token: Token, left: Expr): Expr {
        val arguments = mutableListOf<Expr>()

        if (!parser.match(TokenType.R_PAREN)) {
            do {
                arguments += parser.parseExpr()
            } while (parser.match(TokenType.COMMA))
            parser.eat(TokenType.R_PAREN)
        }

        val rParen = parser.last

        val position = left.span(rParen)

        if (left is PropertyAccessExpr) {
            return InvokeMemberExpr(left.target, left.name, arguments, position)
        } else if (left is IdentExpr) {
            return InvokeLocalExpr(left.name, arguments, position)
        }

        return InvokeExpr(left, arguments, position)
    }
}