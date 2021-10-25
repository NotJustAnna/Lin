package com.github.adriantodt.lin.grammar.parselets.misc

import com.github.adriantodt.lin.ast.node.Expr
import com.github.adriantodt.lin.ast.node.InvalidNode
import com.github.adriantodt.lin.ast.node.Node
import com.github.adriantodt.lin.ast.node.declare.DeclareVariableNode
import com.github.adriantodt.lin.grammar.utils.skipOnlyUntil
import com.github.adriantodt.lin.lexer.TokenType
import com.github.adriantodt.tartar.api.parser.ParserContext
import com.github.adriantodt.tartar.api.parser.PrefixParser
import com.github.adriantodt.tartar.api.parser.SyntaxException
import com.github.adriantodt.tartar.api.parser.Token
import io.github.cafeteriaguild.lin.parser.utils.matchAll

class DeclareVariableParser(val mutable: Boolean) : PrefixParser<TokenType, Node> {
    override fun parse(ctx: ParserContext<TokenType, Node>, token: Token<TokenType>): Node {
        // TODO Variable destructuring goes here (Check Lin/old)
        val ident = ctx.eat(TokenType.IDENTIFIER)
        ctx.skipOnlyUntil(TokenType.ASSIGN)
        if (ctx.match(TokenType.ASSIGN)) {
            ctx.matchAll(TokenType.NL)
            val expr = ctx.parseExpression().let {
                it as? Expr ?: return InvalidNode {
                    section(it.section)
                    child(it)
                    error(SyntaxException("Expected an expression", it.section))
                }
            }
            return DeclareVariableNode(ident.value, mutable, expr, token.section)
        }
        // Variable delegation goes here (Check Lin/old)

        return DeclareVariableNode(ident.value, mutable, null, token.section)
    }
}
