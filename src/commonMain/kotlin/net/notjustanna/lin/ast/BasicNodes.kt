package net.notjustanna.lin.ast

import net.notjustanna.tartar.api.lexer.Section
import net.notjustanna.tartar.api.lexer.Sectional

sealed interface Node : Sectional

sealed interface Expr : Node

data class MultiExpr(val list: List<Node>, val last: Expr, override val section: Section) : Expr

data class MultiNode(val list: List<Node>, override val section: Section) : Node
