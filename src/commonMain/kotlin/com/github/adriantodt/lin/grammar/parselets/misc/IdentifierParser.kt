package com.github.adriantodt.lin.grammar.parselets.misc

import com.github.adriantodt.lin.ast.node.Expr
import com.github.adriantodt.lin.ast.node.InvalidNode
import com.github.adriantodt.lin.ast.node.Node
import com.github.adriantodt.lin.ast.node.access.AssignNode
import com.github.adriantodt.lin.ast.node.access.IdentifierExpr
import com.github.adriantodt.lin.grammar.utils.maybeIgnoreNL
import com.github.adriantodt.lin.grammar.utils.skipOnlyUntil
import com.github.adriantodt.lin.lexer.TokenType
import com.github.adriantodt.tartar.api.parser.ParserContext
import com.github.adriantodt.tartar.api.parser.PrefixParser
import com.github.adriantodt.tartar.api.parser.SyntaxException
import com.github.adriantodt.tartar.api.parser.Token
import com.github.adriantodt.lin.parser.utils.matchAll

object IdentifierParser : PrefixParser<TokenType, Node> {
    override fun parse(ctx: ParserContext<TokenType, Node>, token: Token<TokenType>): Node {
        val name = token.value

        // Modifiers are implemented here (eg. `operator` modifier for functions)

        ctx.skipOnlyUntil(TokenType.ASSIGN)
        if (ctx.match(TokenType.ASSIGN)) {
            ctx.matchAll(TokenType.NL)
            val expr = ctx.parseExpression().let {
                it as? Expr ?: return InvalidNode {
                    section(token.section)
                    child(it)
                    error(SyntaxException("Expected an expression", it.section))
                }
            }
            return AssignNode(name, expr, token.section)
        }
        ctx.maybeIgnoreNL()
        return IdentifierExpr(name, token.section)
    }
}
