package xyz.avarel.lobos.parser.parselets.declarations

import xyz.avarel.lobos.ast.expr.Expr
import xyz.avarel.lobos.ast.expr.misc.ClosureExpr
import xyz.avarel.lobos.ast.types.ArgumentParameterAST
import xyz.avarel.lobos.lexer.Token
import xyz.avarel.lobos.lexer.TokenType
import xyz.avarel.lobos.parser.*

object ClosureParser : PrefixParser {
    override fun parse(parser: Parser, modifiers: List<Modifier>, token: Token): Expr {
        val arguments = mutableMapOf<String, ArgumentParameterAST>()

        if (token.type != TokenType.OR && !parser.match(TokenType.PIPE)) {
            do {
                val isMutable = parser.match(TokenType.MUT)

                val paramIdent = parser.eat(TokenType.IDENT)
                val paramName = paramIdent.string

                parser.eat(TokenType.COLON)
                val type = parser.parseSingleTypeAST()

                if (paramName in arguments) {
                    parser.errors += SyntaxException(
                        "Parameter $paramName has already been declared",
                        paramIdent.section
                    )
                }

                arguments[paramName] = ArgumentParameterAST(isMutable, paramName, type)
            } while (parser.match(TokenType.COMMA))

            parser.eat(TokenType.PIPE)
        }

        val body = if (parser.nextIs(TokenType.L_BRACE)) {
            parser.parseBlock()
        } else {
            parser.parseExpr()
        }

        return ClosureExpr(arguments.values.toList(), body, token.section)
    }
}