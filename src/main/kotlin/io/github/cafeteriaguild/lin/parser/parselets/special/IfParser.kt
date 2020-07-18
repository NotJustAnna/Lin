package io.github.cafeteriaguild.lin.parser.parselets.special

import net.notjustanna.tartar.api.parser.ParserContext
import net.notjustanna.tartar.api.parser.PrefixParser
import net.notjustanna.tartar.api.parser.SyntaxException
import net.notjustanna.tartar.api.parser.Token
import io.github.cafeteriaguild.lin.ast.expr.Expr
import io.github.cafeteriaguild.lin.ast.expr.Node
import io.github.cafeteriaguild.lin.ast.expr.misc.IfExpr
import io.github.cafeteriaguild.lin.ast.expr.misc.IfNode
import io.github.cafeteriaguild.lin.ast.expr.misc.InvalidNode
import io.github.cafeteriaguild.lin.lexer.TokenType
import io.github.cafeteriaguild.lin.parser.utils.matchAll
import io.github.cafeteriaguild.lin.parser.utils.parseBlock
import io.github.cafeteriaguild.lin.parser.utils.skipOnlyUntil

object IfParser : PrefixParser<TokenType, Node> {
    override fun parse(ctx: ParserContext<TokenType, Node>, token: Token<TokenType>): Node {
        ctx.matchAll(TokenType.NL)
        ctx.eat(TokenType.L_PAREN)
        ctx.matchAll(TokenType.NL)
        val condition = ctx.parseExpression().let {
            it as? Expr ?: return InvalidNode {
                section(token.section)
                child(it)
                error(SyntaxException("Expected a node", it.section))
            }
        }
        ctx.matchAll(TokenType.NL)
        ctx.eat(TokenType.R_PAREN)
        ctx.matchAll(TokenType.NL)
        val thenBranch = ctx.parseBlock() ?: ctx.parseExpression()

        ctx.skipOnlyUntil(TokenType.ELSE)
        val elseBranch = if (ctx.match(TokenType.ELSE)) {
            ctx.matchAll(TokenType.NL)
            if (ctx.nextIs(TokenType.IF)) {
                ctx.parseExpression()
            } else {
                ctx.parseBlock() ?: ctx.parseExpression()
            }
        } else {
            null
        }

        if (thenBranch is Expr && elseBranch is Expr) {
            return IfNode(condition, thenBranch, elseBranch, token.section)
        }
        return IfExpr(condition, thenBranch, elseBranch, token.section)
    }
}