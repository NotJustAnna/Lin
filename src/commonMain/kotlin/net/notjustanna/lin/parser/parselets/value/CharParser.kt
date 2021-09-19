package net.notjustanna.lin.parser.parselets.value

import net.notjustanna.lin.ast.CharExpr
import net.notjustanna.lin.ast.InvalidNode
import net.notjustanna.lin.ast.Node
import net.notjustanna.lin.lexer.TokenType
import net.notjustanna.lin.parser.utils.maybeIgnoreNL
import net.notjustanna.tartar.api.parser.ParserContext
import net.notjustanna.tartar.api.parser.PrefixParser
import net.notjustanna.tartar.api.parser.SyntaxException
import net.notjustanna.tartar.api.parser.Token

object CharParser : PrefixParser<TokenType, Node> {
    override fun parse(ctx: ParserContext<TokenType, Node>, token: Token<TokenType>): Node {
        ctx.maybeIgnoreNL()
        val value = token.value
        if (value.length > 1) {
            return InvalidNode {
                section(token.section)
                error(SyntaxException("Too many characters in a character literal", token.section))
            }
        }
        return CharExpr(value.first(), token.section)
    }
}
