package net.notjustanna.lin.grammar.parselets.control

import net.notjustanna.lin.ast.node.Node
import net.notjustanna.lin.ast.node.control.ContinueExpr
import net.notjustanna.lin.lexer.TokenType
import net.notjustanna.tartar.api.parser.ParserContext
import net.notjustanna.tartar.api.parser.PrefixParser
import net.notjustanna.tartar.api.parser.Token

object ContinueParser : PrefixParser<TokenType, Node> {
    override fun parse(ctx: ParserContext<TokenType, Node>, token: Token<TokenType>): Node {
        return ContinueExpr(token.section)
    }
}
