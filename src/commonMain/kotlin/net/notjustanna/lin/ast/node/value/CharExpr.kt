package net.notjustanna.lin.ast.node.value

import net.notjustanna.lin.ast.node.ConstExpr
import net.notjustanna.lin.ast.node.Expr
import net.notjustanna.lin.ast.visitor.NodeMapVisitor
import net.notjustanna.lin.ast.visitor.NodeVisitor
import net.notjustanna.lin.ast.visitor.NodeVisitor1
import net.notjustanna.lin.ast.visitor.NodeVisitorR
import net.notjustanna.tartar.api.lexer.Section

//data class CharExpr(val value: Char, override val section: Section) : ConstExpr {
//    /* @automation-disabled(ast.impl CharExpr,Expr)-start */
//    override fun accept(visitor: NodeVisitor) = visitor.visitCharExpr(this)
//
//    override fun accept(visitor: NodeMapVisitor): Expr = visitor.visitCharExpr(this)
//
//    override fun <R> accept(visitor: NodeVisitorR<R>): R = visitor.visitCharExpr(this)
//
//    override fun <T> accept(visitor: NodeVisitor1<T>, param0: T) = visitor.visitCharExpr(this, param0)
//    /* @automation-end */
//}
