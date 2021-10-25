package net.notjustanna.lin.grammar.parselets.control

import net.notjustanna.lin.ast.node.InvalidNode
import net.notjustanna.lin.ast.node.Node
import net.notjustanna.lin.ast.node.control.CatchBranch
import net.notjustanna.lin.ast.node.control.TryExpr
import net.notjustanna.lin.grammar.utils.parseBlock
import net.notjustanna.lin.grammar.utils.skipOnlyUntil
import net.notjustanna.lin.lexer.TokenType
import net.notjustanna.tartar.api.parser.ParserContext
import net.notjustanna.tartar.api.parser.PrefixParser
import net.notjustanna.tartar.api.parser.SyntaxException
import net.notjustanna.tartar.api.parser.Token

object TryParser : PrefixParser<TokenType, Node> {
    override fun parse(ctx: ParserContext<TokenType, Node>, token: Token<TokenType>): Node {
        ctx.skipOnlyUntil(TokenType.L_BRACE)
        ctx.eat(TokenType.L_BRACE)
        val tryBranch = ctx.parseBlock(braceConsumed = true) ?: error("Impossible to be null.")
        var catchBranch: CatchBranch? = null
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
