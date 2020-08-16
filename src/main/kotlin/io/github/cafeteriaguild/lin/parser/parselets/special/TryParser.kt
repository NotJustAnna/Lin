package io.github.cafeteriaguild.lin.parser.parselets.special

import net.notjustanna.tartar.api.parser.ParserContext
import net.notjustanna.tartar.api.parser.PrefixParser
import net.notjustanna.tartar.api.parser.SyntaxException
import net.notjustanna.tartar.api.parser.Token
import io.github.cafeteriaguild.lin.ast.node.Node
import io.github.cafeteriaguild.lin.ast.node.misc.InvalidNode
import io.github.cafeteriaguild.lin.ast.node.misc.TryExpr
import io.github.cafeteriaguild.lin.lexer.TokenType
import io.github.cafeteriaguild.lin.parser.utils.parseBlock
import io.github.cafeteriaguild.lin.parser.utils.skipOnlyUntil

object TryParser : PrefixParser<TokenType, Node> {
    override fun parse(ctx: ParserContext<TokenType, Node>, token: Token<TokenType>): Node {
        ctx.skipOnlyUntil(TokenType.L_BRACE)
        ctx.eat(TokenType.L_BRACE)
        val tryBranch = ctx.parseBlock(braceConsumed = true) ?: error("Impossible to be null.")
        var catchBranch: TryExpr.CatchBranch? = null
        var finallyBranch: Node? = null

        var mark = ctx.index
        loop@ while (catchBranch == null || finallyBranch == null) {
            ctx.skipOnlyUntil(TokenType.IDENTIFIER)
            if (ctx.nextIs(TokenType.IDENTIFIER)) {
                when (ctx.eat().value) {
                    "catch" -> {
                        val catchToken = ctx.last
                        ctx.skipOnlyUntil(TokenType.L_PAREN)
                        ctx.eat(TokenType.L_PAREN)
                        ctx.skipOnlyUntil(TokenType.IDENTIFIER)
                        val name = ctx.eat(TokenType.IDENTIFIER).value
                        ctx.skipOnlyUntil(TokenType.R_PAREN)
                        ctx.eat(TokenType.R_PAREN)
                        ctx.skipOnlyUntil(TokenType.L_BRACE)
                        ctx.eat(TokenType.L_BRACE)
                        val branch = ctx.parseBlock(braceConsumed = true) ?: error("Impossible to be null.")
                        mark = ctx.index
                        catchBranch?.let {
                            return InvalidNode {
                                section(catchToken.section)
                                child(tryBranch, it.branch, branch)
                                error(SyntaxException("Catch branch already defined", it.branch.section))
                            }
                        }
                        catchBranch = TryExpr.CatchBranch(name, branch)
                    }
                    "finally" -> {
                        ctx.skipOnlyUntil(TokenType.L_BRACE)
                        ctx.eat(TokenType.L_BRACE)
                        finallyBranch = ctx.parseBlock(braceConsumed = true) ?: error("Impossible to be null.")
                        break@loop
                    }
                    else -> {
                        ctx.index = mark
                        break@loop
                    }
                }
            } else {
                ctx.index = mark
                break
            }
        }
        return TryExpr(tryBranch, catchBranch, finallyBranch, token.section)
    }
}