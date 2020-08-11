package io.github.cafeteriaguild.lin.parser

import net.notjustanna.tartar.api.parser.ParserContext
import net.notjustanna.tartar.api.parser.Token
import net.notjustanna.tartar.createGrammar
import io.github.cafeteriaguild.lin.ast.LinModifier
import io.github.cafeteriaguild.lin.ast.node.Node
import io.github.cafeteriaguild.lin.ast.node.declarations.*
import io.github.cafeteriaguild.lin.ast.node.nodes.ObjectExpr
import io.github.cafeteriaguild.lin.lexer.TokenType
import io.github.cafeteriaguild.lin.parser.parselets.declarations.EnumClassParser

object ModifierHandler {
    fun handle(ctx: ParserContext<TokenType, Node>, map: Map<LinModifier, Token<TokenType>>): Node {
        if (ctx.nextIs(TokenType.CLASS)) {
            when {
                map.containsKey(LinModifier.ENUM) -> {
                    return apply(ctx.withGrammar(specialGrammar).parseExpression(), map)
                }
            }
        }
        return apply(ctx.parseExpression(), map)
    }

    private fun apply(node: Node, map: Map<LinModifier, Token<TokenType>>): Node {
        val set = map.keys.toSet()
        when (node) {
            is DeclareObjectNode -> {
                return DeclareObjectNode(
                    node.name, node.implements, node.body, node.section, set + node.modifiers
                )
            }
            is ObjectExpr -> {
                val companionToken = map[LinModifier.COMPANION]
                if (companionToken != null) {
                    return DeclareObjectNode("Companion", node.implements, node.body, companionToken.section, set)
                }
            }
            is DeclareClassNode -> {
                return DeclareClassNode(
                    node.name, node.parameters, node.implements, node.body, node.section, set + node.modifiers
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
                    node.name, node.implements, node.body, node.section, set + node.modifiers
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
            prefix(TokenType.CLASS, EnumClassParser, override = true)
        }
    }
}