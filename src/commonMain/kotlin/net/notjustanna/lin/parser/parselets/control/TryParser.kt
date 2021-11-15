package net.notjustanna.lin.parser.parselets.control

import net.notjustanna.lin.ast.node.InvalidNode
import net.notjustanna.lin.ast.node.Node
import net.notjustanna.lin.ast.node.control.CatchBranch
import net.notjustanna.lin.ast.node.control.TryExpr
import net.notjustanna.lin.lexer.TokenType
import net.notjustanna.lin.parser.utils.parseBlock
import net.notjustanna.lin.parser.utils.skipOnlyUntil
import net.notjustanna.tartar.api.grammar.PrefixParselet
import net.notjustanna.tartar.api.parser.ParserContext
import net.notjustanna.tartar.api.parser.StringToken
import net.notjustanna.tartar.api.parser.SyntaxException
import net.notjustanna.tartar.api.parser.Token

public object TryParser : PrefixParselet<TokenType, Token<TokenType>, Node> {
    override fun parse(ctx: ParserContext<TokenType, Token<TokenType>, Node>, token: Token<TokenType>): Node {
        ctx.skipOnlyUntil(TokenType.L_BRACE)
        ctx.eat(TokenType.L_BRACE)
        val tryBranch = ctx.parseBlock(braceConsumed = true) ?: error("Impossible to be null.")
        var catchBranch: CatchBranch? = null
        var finallyBranch: Node? = null

        var mark = ctx.index
        loop@ while (catchBranch == null || finallyBranch == null) {
            ctx.skipOnlyUntil(TokenType.IDENTIFIER)
            if (ctx.nextIs(TokenType.IDENTIFIER)) {
                when ((ctx.eat() as StringToken).value) {
                    "catch" -> {
                        val catchToken = ctx.last
                        ctx.skipOnlyUntil(TokenType.L_PAREN)
                        val name = if (ctx.match(TokenType.L_PAREN)) {
                            ctx.eat(TokenType.L_PAREN)
                            ctx.skipOnlyUntil(TokenType.IDENTIFIER)
                            val value = if (ctx.match(TokenType.IDENTIFIER)) (ctx.last as StringToken).value else null
                            ctx.skipOnlyUntil(TokenType.R_PAREN)
                            ctx.eat(TokenType.R_PAREN)
                            value
                        } else null
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
                        catchBranch = CatchBranch(name, branch)
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
