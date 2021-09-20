package com.github.adriantodt.lin.ast

import com.github.adriantodt.tartar.api.lexer.Section

class DeclareVariableNode(val name: String, val mutable: Boolean, val value: Expr?, override val section: Section): Node

class DeclareFunctionExpr(val name: String, val value: FunctionExpr, override val section: Section): Expr
