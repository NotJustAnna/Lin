package io.github.cafeteriaguild.lin.parser.parselets.nodes

import net.notjustanna.tartar.api.parser.ParserContext
import net.notjustanna.tartar.api.parser.PrefixParser
import net.notjustanna.tartar.api.parser.SyntaxException
import net.notjustanna.tartar.api.parser.Token
import io.github.cafeteriaguild.lin.ast.expr.Node
import io.github.cafeteriaguild.lin.ast.expr.misc.InvalidNode
import io.github.cafeteriaguild.lin.ast.expr.nodes.LambdaExpr
import io.github.cafeteriaguild.lin.lexer.TokenType
import io.github.cafeteriaguild.lin.parser.utils.matchAll
import io.github.cafeteriaguild.lin.parser.utils.parseBlock

object LambdaParser : PrefixParser<TokenType, Node> {

    override fun parse(ctx: ParserContext<TokenType, Node>, token: Token<TokenType>): Node {

        val parameters = mutableListOf<LambdaExpr.Parameter>()

        if (ctx.match(TokenType.PIPE)) {
            do {
                ctx.matchAll(TokenType.NL)
                if (ctx.match(TokenType.L_PAREN)) {
                    val names = mutableListOf<String>()
                    do {
                        ctx.matchAll(TokenType.NL)
                        val paramIdent = ctx.eat(TokenType.IDENTIFIER)
                        ctx.matchAll(TokenType.NL)
                        names += paramIdent.value
                    } while (ctx.match(TokenType.COMMA))
                    ctx.matchAll(TokenType.NL)
                    ctx.eat(TokenType.R_PAREN)
                    parameters += LambdaExpr.Parameter.Destructured(names)
                } else {
                    val paramIdent = ctx.eat(TokenType.IDENTIFIER)
                    parameters += LambdaExpr.Parameter.Named(paramIdent.value)
                }
                ctx.matchAll(TokenType.NL)

            } while (ctx.match(TokenType.COMMA))
            ctx.matchAll(TokenType.NL)
            ctx.eat(TokenType.PIPE)
            ctx.matchAll(TokenType.NL)
            ctx.eat(TokenType.ARROW)
        }
        ctx.matchAll(TokenType.NL)
        val expr = ctx.parseBlock(braceConsumed = true) ?: return InvalidNode {
            section(token.section)
            error(SyntaxException("Couldn't parse function's block, found ${ctx.peek()}", token.section))
        }
        return LambdaExpr(parameters, expr, token.section)
    }
}