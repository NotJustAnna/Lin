package io.github.cafeteriaguild.lin.parser.parselets.special

import net.notjustanna.tartar.api.parser.ParserContext
import net.notjustanna.tartar.api.parser.PrefixParser
import net.notjustanna.tartar.api.parser.SyntaxException
import net.notjustanna.tartar.api.parser.Token
import io.github.cafeteriaguild.lin.ast.expr.Expr
import io.github.cafeteriaguild.lin.ast.expr.Node
import io.github.cafeteriaguild.lin.ast.expr.misc.InvalidExpr
import io.github.cafeteriaguild.lin.ast.expr.misc.UnitNode
import io.github.cafeteriaguild.lin.ast.expr.nodes.ReturnExpr
import io.github.cafeteriaguild.lin.lexer.TokenType

object ReturnParser : PrefixParser<TokenType, Expr> {
    override fun parse(ctx: ParserContext<TokenType, Expr>, token: Token<TokenType>): Expr {
        val node = if (ctx.matchAny(TokenType.NL, TokenType.SEMICOLON)) {
            UnitNode(token.section)
        } else {
            ctx.parseExpression().let {
                it as? Node ?: return InvalidExpr {
                    section(token.section)
                    child(it)
                    error(SyntaxException("Expected a node but got a statement instead.", it.section))
                }
            }
        }

        return ReturnExpr(node, token.section)
    }
}