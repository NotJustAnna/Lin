package com.github.adriantodt.lin.grammar.parselets.control

import com.github.adriantodt.lin.ast.node.InvalidNode
import com.github.adriantodt.lin.ast.node.Node
import com.github.adriantodt.lin.ast.node.control.CatchBranch
import com.github.adriantodt.lin.ast.node.control.TryExpr
import com.github.adriantodt.lin.grammar.utils.parseBlock
import com.github.adriantodt.lin.grammar.utils.skipOnlyUntil
import com.github.adriantodt.lin.lexer.TokenType
import com.github.adriantodt.tartar.api.parser.ParserContext
import com.github.adriantodt.tartar.api.parser.PrefixParser
import com.github.adriantodt.tartar.api.parser.SyntaxException
import com.github.adriantodt.tartar.api.parser.Token

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
                        val name = if (ctx.match(TokenType.L_PAREN)) {
                            ctx.eat(TokenType.L_PAREN)
                            ctx.skipOnlyUntil(TokenType.IDENTIFIER)
                            val value = if (ctx.match(TokenType.IDENTIFIER)) ctx.last.value else null
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
