package io.github.cafeteriaguild.lin.parser.parselets.nodes

import net.notjustanna.tartar.api.parser.ParserContext
import net.notjustanna.tartar.api.parser.PrefixParser
import net.notjustanna.tartar.api.parser.Token
import io.github.cafeteriaguild.lin.ast.node.Node
import io.github.cafeteriaguild.lin.ast.node.nodes.FloatExpr
import io.github.cafeteriaguild.lin.lexer.TokenType
import io.github.cafeteriaguild.lin.parser.utils.maybeIgnoreNL

object FloatParser : PrefixParser<TokenType, Node> {
    override fun parse(ctx: ParserContext<TokenType, Node>, token: Token<TokenType>): Node {
        ctx.maybeIgnoreNL()
        return FloatExpr(token.value.toFloat(), token.section)
    }
}