package net.notjustanna.lin.parser.parselets.misc

import net.notjustanna.lin.ast.node.Expr
import net.notjustanna.lin.ast.node.InvalidNode
import net.notjustanna.lin.ast.node.Node
import net.notjustanna.lin.ast.node.misc.EnsureNotNullExpr
import net.notjustanna.lin.lexer.TokenType
import net.notjustanna.lin.parser.Precedence
import net.notjustanna.lin.parser.utils.maybeIgnoreNL
import net.notjustanna.tartar.api.grammar.InfixParselet
import net.notjustanna.tartar.api.parser.ParserContext
import net.notjustanna.tartar.api.parser.SyntaxException
import net.notjustanna.tartar.api.parser.Token

public object DoubleBangParser : InfixParselet<TokenType, Token<TokenType>, Node> {
    override val precedence: Int = Precedence.POSTFIX

    override fun parse(
        ctx: ParserContext<TokenType, Token<TokenType>, Node>,
        left: Node,
        token: Token<TokenType>
    ): Node {
        if (left !is Expr) {
            return InvalidNode {
                section(token.section)
                child(left)
                error(SyntaxException("Expected an expression", left.section))
            }
        }
        ctx.maybeIgnoreNL()
        return EnsureNotNullExpr(left, token.section)
    }
}
