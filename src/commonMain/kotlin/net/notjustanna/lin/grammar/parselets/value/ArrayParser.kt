package net.notjustanna.lin.grammar.parselets.value

import net.notjustanna.lin.ast.node.Expr
import net.notjustanna.lin.ast.node.InvalidNode
import net.notjustanna.lin.ast.node.Node
import net.notjustanna.lin.ast.node.value.ArrayExpr
import net.notjustanna.lin.lexer.TokenType
import net.notjustanna.tartar.api.parser.ParserContext
import net.notjustanna.tartar.api.parser.PrefixParser
import net.notjustanna.tartar.api.parser.SyntaxException
import net.notjustanna.tartar.api.parser.Token
import io.github.cafeteriaguild.lin.parser.utils.matchAll

object ArrayParser : PrefixParser<TokenType, Node> {
    override fun parse(ctx: ParserContext<TokenType, Node>, token: Token<TokenType>): Node {
        val contents = mutableListOf<Expr>()

        ctx.matchAll(TokenType.NL)
        if (!ctx.match(TokenType.R_BRACKET)) {
            do {
                ctx.matchAll(TokenType.NL)
                contents += ctx.parseExpression().let {
                    it as? Expr ?: return InvalidNode {
                        section(token.section)
                        child(it)
                        error(SyntaxException("Expected an expression", it.section))
                    }
                }
                ctx.matchAll(TokenType.NL)
            } while (ctx.match(TokenType.COMMA))
            ctx.eat(TokenType.R_BRACKET)
        }
        return ArrayExpr(contents, token.section)
    }
}
