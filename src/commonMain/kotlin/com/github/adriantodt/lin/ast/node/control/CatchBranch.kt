package com.github.adriantodt.lin.ast.node.control

import com.github.adriantodt.lin.ast.node.Node

/**
 * This is part of [TryExpr].
 */
public data class CatchBranch(
    val caughtName: String?, val branch: Node
)
