package io.github.cafeteriaguild.lin.parser.parselets.nodes

import net.notjustanna.tartar.api.parser.ParserContext
import net.notjustanna.tartar.api.parser.PrefixParser
import net.notjustanna.tartar.api.parser.Token
import net.notjustanna.tartar.createGrammar
import io.github.cafeteriaguild.lin.ast.LinModifier
import io.github.cafeteriaguild.lin.ast.expr.Node
import io.github.cafeteriaguild.lin.ast.expr.access.AssignNode
import io.github.cafeteriaguild.lin.ast.expr.declarations.*
import io.github.cafeteriaguild.lin.ast.expr.nodes.IdentifierExpr
import io.github.cafeteriaguild.lin.ast.expr.nodes.ObjectExpr
import io.github.cafeteriaguild.lin.lexer.TokenType
import io.github.cafeteriaguild.lin.lexer.TokenType.*
import io.github.cafeteriaguild.lin.parser.linStdGrammar
import io.github.cafeteriaguild.lin.parser.parselets.declarations.EnumClassParser
import io.github.cafeteriaguild.lin.parser.utils.matchAll
import io.github.cafeteriaguild.lin.parser.utils.maybeIgnoreNL

object IdentifierParser : PrefixParser<TokenType, Node> {
    override fun parse(ctx: ParserContext<TokenType, Node>, token: Token<TokenType>): Node {
        val name = token.value

        if (token.value in LinModifier.names) {
            val ahead = listOf(token) + ctx.peekAheadUntil(OBJECT, FUN, CLASS, INTERFACE, VAL, VAR)
            if (ahead.all { it.type == IDENTIFIER && it.value in LinModifier.names || it.type == NL }) {
                ctx.matchAll(IDENTIFIER)
                return parseModifiers(ctx, LinModifier.parse(ahead.filter { it.type == IDENTIFIER }))
            }
        }

        if (ctx.match(ASSIGN)) {
            val expr = ctx.parseExpression()

            return AssignNode(name, expr, token.section)
        }
        ctx.maybeIgnoreNL()
        return IdentifierExpr(name, token.section)
    }

    private fun parseModifiers(ctx: ParserContext<TokenType, Node>, map: Map<LinModifier, Token<TokenType>>): Node {
        if (ctx.nextIs(CLASS)) {
            when {
                map.containsKey(LinModifier.ENUM) -> {
                    return applyModifiers(ctx.withGrammar(specialGrammar).parseExpression(), map)
                }
            }
        }
        return applyModifiers(ctx.parseExpression(), map)
    }

    fun applyModifiers(node: Node, map: Map<LinModifier, Token<TokenType>>): Node {
        val set = map.keys.toSet()
        when (node) {
            is DeclareObjectNode -> {
                return DeclareObjectNode(
                    node.name, node.obj, node.section, set + node.modifiers
                )
            }
            is ObjectExpr -> {
                val companionToken = map[LinModifier.COMPANION]
                if (companionToken != null) {
                    return DeclareObjectNode("Companion", node, companionToken.section, set)
                }
            }
            is DeclareClassNode -> {
                return DeclareClassNode(
                    node.name, node.body, node.section, set + node.modifiers
                )
            }
            is DeclareEnumClassNode -> {
                return DeclareEnumClassNode(
                    node.name, node.values, node.body, node.section, set + node.modifiers
                )
            }
            is DeclareFunctionNode -> {
                return DeclareFunctionNode(
                    node.name, node.function, node.section, set + node.modifiers
                )
            }
            is DeclareInterfaceNode -> {
                return DeclareInterfaceNode(
                    node.name, node.body, node.section, set + node.modifiers
                )
            }
            is DeclareVariableNode -> {
                return DeclareVariableNode(
                    node.name, node.mutable, node.value, node.section, set + node.modifiers
                )
            }
            is DestructuringVariableNode -> {
                return DestructuringVariableNode(
                    node.names, node.mutable, node.value, node.section, set + node.modifiers
                )
            }
        }
        return node
    }

    private val specialGrammar by lazy {
        createGrammar<TokenType, Node> {
            import(true, linStdGrammar)
            prefix(CLASS, EnumClassParser, override = true)
        }
    }
}
