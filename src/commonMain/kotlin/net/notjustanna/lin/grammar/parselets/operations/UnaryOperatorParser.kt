package net.notjustanna.lin.grammar.parselets.operations

import net.notjustanna.lin.ast.node.Expr
import net.notjustanna.lin.ast.node.InvalidNode
import net.notjustanna.lin.ast.node.Node
import net.notjustanna.lin.ast.node.misc.UnaryOperation
import net.notjustanna.lin.grammar.utils.maybeIgnoreNL
import net.notjustanna.lin.lexer.TokenType
import net.notjustanna.lin.parser.Precedence
import net.notjustanna.lin.utils.UnaryOperationType
import net.notjustanna.tartar.api.parser.ParserContext
import net.notjustanna.tartar.api.parser.PrefixParser
import net.notjustanna.tartar.api.parser.SyntaxException
import net.notjustanna.tartar.api.parser.Token
import net.notjustanna.lin.parser.utils.matchAll

class UnaryOperatorParser(private val operator: UnaryOperationType) : PrefixParser<TokenType, Node> {
    override fun parse(ctx: ParserContext<TokenType, Node>, token: Token<TokenType>): Node {
        ctx.matchAll(TokenType.NL)
        val target = ctx.parseExpression(Precedence.PREFIX).let {
            it as? Expr ?: return InvalidNode {
                section(token.section)
                child(it)
                error(SyntaxException("Expected an expression", it.section))
            }
        }
        ctx.maybeIgnoreNL()
        return UnaryOperation(target, operator, token.section)
    }
}
