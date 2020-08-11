//package io.github.cafeteriaguild.lin.rt.lib.lang
//
//import io.github.cafeteriaguild.lin.ast.LinModifier
//import io.github.cafeteriaguild.lin.ast.node.Declaration
//import io.github.cafeteriaguild.lin.ast.node.ObjectDeclaration
//import io.github.cafeteriaguild.lin.ast.node.declarations.DeclareObjectNode
//import io.github.cafeteriaguild.lin.rt.LinInterpreter
//import io.github.cafeteriaguild.lin.rt.exc.LinException
//import io.github.cafeteriaguild.lin.rt.lib.LObj
//import io.github.cafeteriaguild.lin.rt.scope.BasicScope
//import io.github.cafeteriaguild.lin.rt.lib.nativelang.properties.Property
//import io.github.cafeteriaguild.lin.rt.scope.Scope
//
//class LClass(
//    val name: String,
//    interpreter: LinInterpreter,
//    param: Scope,
//    body: List<Declaration>
//) : LObject {
//    private val scope = BasicScope(param)
//    private val companion: LObj?
//    private val declarations: List<Declaration>
//
//    init {
//
//        val (now, later) = body.partition { it is ObjectDeclaration }
//        var companionName: String? = null
//        for (declaration in now) {
//            if (declaration is DeclareObjectNode && LinModifier.COMPANION in declaration.modifiers) {
//                if (companionName == null) {
//                    companionName = declaration.name
//                } else {
//                    throw LinException("Multiple companion objects declared")
//                }
//            }
//        }
//        for (declaration in now) {
//            declaration.accept(interpreter, scope)
//        }
//        companion = if (companionName == null) null else scope.properties.getValue(companionName).get()
//        declarations = later
//    }
//
//    override fun propertyOf(name: String): Property {
//        return scope.properties[name] ?: companion?.propertyOf(name)
//    }
//
//    override fun invoke(args: List<LObj>): LObj {
//        return LObject(name, LinInterpreter(), scope, declarations)
//    }
//}
