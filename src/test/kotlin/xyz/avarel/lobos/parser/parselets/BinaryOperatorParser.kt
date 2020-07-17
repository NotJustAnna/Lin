package xyz.avarel.lobos.parser.parselets

import xyz.avarel.lobos.ast.expr.Expr
import xyz.avarel.lobos.ast.expr.ops.BinaryOperation
import xyz.avarel.lobos.ast.expr.ops.BinaryOperationType
import xyz.avarel.lobos.lexer.Token
import xyz.avarel.lobos.parser.Parser

class BinaryOperatorParser(precedence: Int, val operator: BinaryOperationType, leftAssoc: Boolean = true) :
    BinaryParser(precedence, leftAssoc) {
    override fun parse(parser: Parser, token: Token, left: Expr): Expr {
        val right = parser.parseExpr(precedence - if (leftAssoc) 0 else 1)
        return BinaryOperation(left, right, operator, left.span(right))
    }
}