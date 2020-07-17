package xyz.avarel.lobos.parser.parselets

import xyz.avarel.lobos.ast.expr.Expr
import xyz.avarel.lobos.ast.expr.ops.UnaryOperation
import xyz.avarel.lobos.ast.expr.ops.UnaryOperationType
import xyz.avarel.lobos.lexer.Token
import xyz.avarel.lobos.parser.Modifier
import xyz.avarel.lobos.parser.Parser
import xyz.avarel.lobos.parser.Precedence
import xyz.avarel.lobos.parser.PrefixParser

class UnaryOperatorParser(val operator: UnaryOperationType) : PrefixParser {
    override fun parse(parser: Parser, modifiers: List<Modifier>, token: Token): Expr {
        val target = parser.parseExpr(Precedence.PREFIX)
        return UnaryOperation(target, operator, token.span(target))
    }
}