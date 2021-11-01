package net.notjustanna.lin.grammar.parselets.value

//object CharParser : PrefixParser<TokenType, Node> {
//    override fun parse(ctx: ParserContext<TokenType, Node>, token: Token<TokenType>): Node {
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
