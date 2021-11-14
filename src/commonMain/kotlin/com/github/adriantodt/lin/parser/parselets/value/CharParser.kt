package com.github.adriantodt.lin.parser.parselets.value

//object CharParser : PrefixParselet<TokenType, Token<TokenType>, Node> {
//    override fun parse(ctx: ParserContext<TokenType, Token<TokenType>, Node>, token: Token<TokenType>): Node {
//        ctx.maybeIgnoreNL()
//        val value = token.value
//        if (value.length > 1) {
//            return InvalidNode {
//                section(token.section)
//                error(SyntaxException("Too many characters in a character literal", token.section))
//            }
//        }
//        return CharExpr(value.first(), token.section)
//    }
//}
