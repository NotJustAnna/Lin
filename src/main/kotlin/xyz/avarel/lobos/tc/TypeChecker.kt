package xyz.avarel.lobos.tc

import xyz.avarel.lobos.ast.expr.Expr
import xyz.avarel.lobos.ast.expr.ExprVisitor
import xyz.avarel.lobos.ast.expr.access.*
import xyz.avarel.lobos.ast.expr.declarations.*
import xyz.avarel.lobos.ast.expr.files.FileModuleExpr
import xyz.avarel.lobos.ast.expr.files.FolderModuleExpr
import xyz.avarel.lobos.ast.expr.invoke.InvokeExpr
import xyz.avarel.lobos.ast.expr.invoke.InvokeLocalExpr
import xyz.avarel.lobos.ast.expr.invoke.InvokeMemberExpr
import xyz.avarel.lobos.ast.expr.misc.*
import xyz.avarel.lobos.ast.expr.nodes.*
import xyz.avarel.lobos.ast.expr.ops.BinaryOperation
import xyz.avarel.lobos.ast.expr.ops.BinaryOperationType
import xyz.avarel.lobos.ast.expr.ops.UnaryOperation
import xyz.avarel.lobos.ast.expr.ops.UnaryOperationType
import xyz.avarel.lobos.ast.types.TypeAST
import xyz.avarel.lobos.lexer.Section
import xyz.avarel.lobos.parser.TypeException
import xyz.avarel.lobos.parser.mergeAll
import xyz.avarel.lobos.tc.base.*
import xyz.avarel.lobos.tc.complex.ArrayType
import xyz.avarel.lobos.tc.complex.FunctionType
import xyz.avarel.lobos.tc.complex.MapType
import xyz.avarel.lobos.tc.complex.TupleType
import xyz.avarel.lobos.tc.generics.GenericBodyType
import xyz.avarel.lobos.tc.generics.GenericParameter
import xyz.avarel.lobos.tc.generics.GenericType
import xyz.avarel.lobos.tc.module.ModuleType
import xyz.avarel.lobos.tc.scope.ScopeContext
import xyz.avarel.lobos.tc.scope.StmtContext
import xyz.avarel.lobos.tc.scope.VariableInfo

// TODO RETHINK ALL OF SMART IF

open class TypeChecker(
    val scope: ScopeContext,
    val stmt: StmtContext,
    val deferBody: Boolean,
    val errorHandler: (message: String, section: Section) -> Unit
) : ExprVisitor<Type?> {
    override fun visit(expr: UseExpr): Type? {
        var target: Type? = null
        for (name in expr.list) {
            target = if (target == null) {
                scope.getDeclaration(name)?.type
            } else {
                target.getMember(name)?.type
            }

            if (target == null) {
                errorHandler("$name does not exist", expr.section)
                return null
            }
        }

        val name = expr.list.last()
        if (name in scope.variables) {
            errorHandler("Reference $name have been already declared", expr.section)
            return null
        }

        scope.declare(name, target!!, false)
        scope.variableImportMap[
                name] = expr.list
        return null
    }

    override fun visit(expr: FolderModuleExpr): Type? {
        if (expr.name in scope.variables) {
            if (!scope.getDeclaration(expr.name)!!.mutable) {
                // if a module is mutable in this scope, it's body's typecheck is being deferred
                errorHandler("Module ${expr.name} has already been declared", expr.section)
                return null
            }
        }

        val subScope = scope.subContext(false)
        val type = ModuleType.Local(expr.name).also { it.members = subScope.variables }

        scope.declare(expr.name, type, deferBody)

        expr.folderModules.forEach { it.visitStmt(subScope, deferBody = true) }

        expr.fileModules.forEach { it.visitStmt(subScope, deferBody = true) }

        if (!deferBody) {
            val moduleSubScope = subScope.subContext(false)
            moduleSubScope.declare("super", type, false)

            expr.folderModules.forEach { it.visitStmt(moduleSubScope) }

            // check function bodies //
            expr.fileModules.forEach { it.visitStmt(subScope) }
        }

        return null
    }

    override fun visit(expr: FileModuleExpr): Type? {
        return visit(expr as DeclareModuleExpr)
    }

    override fun visit(expr: NullExpr) = NullType
    override fun visit(expr: I32Expr) = I32Type
    override fun visit(expr: I64Expr) = I64Type
    override fun visit(expr: F64Expr) = F64Type
    override fun visit(expr: InvalidExpr) = InvalidType
    override fun visit(expr: StringExpr) = StrType
    override fun visit(expr: BooleanExpr) = BoolType

    override fun visit(expr: DeclareModuleExpr): Type? {
        if (expr.name in scope.variables) {
            if (!scope.getDeclaration(expr.name)!!.mutable) {
                // if a module is mutable in this scope, it's body's typecheck is being deferred
                errorHandler("Module ${expr.name} has already been declared", expr.section)
                return null
            }
        }

        val subScope = scope.subContext(false)
        val type = ModuleType.Local(expr.name).also { it.members = subScope.variables }

        scope.declare(expr.name, type, deferBody)

        expr.declarationsAST.let { declarations ->
            // defer modules //
            declarations.modules.forEach { it.visitStmt(subScope, deferBody = true) }

            // use statements
            declarations.uses.forEach { it.visitStmt(subScope) }

            // defer functions //
            declarations.functions.forEach { it.visitStmt(subScope, deferBody = true) }

            if (!deferBody) {
                // check lets //
                declarations.variables.forEach { it.visitStmt(subScope) }
                // check modules modules //

                val moduleSubScope = subScope.subContext(false)
                moduleSubScope.declare("super", type, false)

                declarations.modules.forEach { it.visitStmt(moduleSubScope) }

                // check function bodies //
                declarations.functions.forEach { it.visitStmt(subScope) }
            }
        }

        return null
    }

    override fun visit(expr: DeclareFunctionExpr): Type? {
        if (expr.name in scope.variables) {
            if (!scope.getDeclaration(expr.name)!!.mutable) {
                // if a function is mutable in this scope, it's body's typecheck is being deferred
                errorHandler("Reference ${expr.name} has already been declared", expr.section)
                return null
            }
        }

        val genericParameters = expr.generics.map { GenericParameter(it.name, it.parentType?.resolve(scope)) }
        val argumentScope = scope.subContext()

        genericParameters.forEach {
            argumentScope.putType(it.name, GenericType(it))
        }

        val argumentTypes = expr.arguments.map { it.type.resolve(argumentScope) }
        val returnType = expr.returnType.resolve(argumentScope)

        val type = FunctionType(argumentTypes, returnType)
        type.genericParameters = genericParameters

        if (!deferBody) {
            val bodyScope = scope.subContext(false, returnType)

            genericParameters.forEach {
                bodyScope.putType(it.name, GenericBodyType(it))
            }

            expr.arguments.zip(argumentTypes) { ast, argType ->
                bodyScope.declare(ast.name, argType.transformToBodyType(), false)
            }

            bodyScope.declare(expr.name, type, false)

            val resultType = expr.body.visitValue(bodyScope)
            if (!bodyScope.terminates) {
                checkType(
                    returnType,
                    resultType,
                    if (expr.body is MultiExpr) {
                        expr.body.list.last().section
                    } else {
                        expr.body.section
                    }
                )
            }
        }

        scope.declare(expr.name, type, deferBody)
        return null
    }

    override fun visit(expr: TypeAliasExpr): Type? {
        val typeAliased = if (expr.generics.isNotEmpty()) {
            val genericParameters = expr.generics.map {
                GenericParameter(it.name, it.parentType?.resolve(scope))
            }

            val scope = scope.subContext()

            genericParameters.forEach {
                scope.putType(it.name, GenericType(it))
            }

            expr.type.resolve(scope).also { typeAliased ->
                if (typeAliased is TypeTemplate) {
                    if (typeAliased.genericParameters.size != genericParameters.size || !typeAliased.genericParameters.containsAll(
                            genericParameters
                        )
                    ) {
                        errorHandler("Extraneous type parameters are not allowed for type aliases", expr.section)
                        return null
                    } else {
                        typeAliased.genericParameters = genericParameters
                    }
                } else {
                    errorHandler("${expr.type} is not a generic type", expr.type.section)
                    return null
                }
            }
        } else {
            expr.type.resolve(scope)
        }

        scope.putType(expr.name, typeAliased)

        return null
    }

    override fun visit(expr: DeclareLetExpr): Type? {
        val exprType = expr.value.visitValue(scope)
        expr.pattern.accept(PatternTypeBinder(this, exprType, scope, true))
        return null
    }

    override fun visit(expr: AssignExpr): Type? {
        val (type, mutable) = scope.getDeclaration(expr.name) ?: let {
            errorHandler("Reference ${expr.name} does not exist in this scope", expr.section)
            return null
        }

        if (!mutable) {
            errorHandler("Reference ${expr.name} is not mutable", expr.section)
            return null
        }

        val exprType = expr.value.visitValue(scope)
        if (checkType(type, exprType, expr.value.section)) {
            scope.assume(expr.name, exprType)
        }

        return null
    }

    override fun visit(expr: IdentExpr): Type {
        val key = expr.name
        return stmt.getAssumption(key) ?: scope.getAssumption(key) ?: let {
            errorHandler("Reference $key does not exist in this scope", expr.section)
            return InvalidType
        }
    }

    override fun visit(expr: ClosureExpr): Type? {
        val bodyScope = scope.subContext(false, AnyType)

        val arguments = expr.arguments.map {
            it.type.resolve(scope).also { type ->
                bodyScope.declare(it.name, type.transformToBodyType(), false)
            }
        }

        val resultType = expr.body.visitValue(bodyScope)

        return FunctionType(arguments, resultType)
    }

    override fun visit(expr: TupleExpr): Type {
        return when {
            expr.list.isEmpty() -> UnitType
            else -> TupleType(expr.list.map { it.visitValue(scope) })
        }
    }

    override fun visit(expr: ListLiteralExpr): Type? {
        return if (expr.list.isEmpty()) {
            ArrayType(NeverType)
        } else {
            val valueType = expr.list.map { it.visitValue(scope) }.reduce(Type::commonSuperTypeWith)
            ArrayType(valueType)
        }
    }

    override fun visit(expr: MapLiteralExpr): Type? {
        return if (expr.map.isEmpty()) {
            MapType(NeverType, NeverType)
        } else {
            val keyType = expr.map.keys.map { it.visitValue(scope) }.reduce(Type::commonSuperTypeWith)
            val valueType = expr.map.values.map { it.visitValue(scope) }.reduce(Type::commonSuperTypeWith)
            MapType(keyType, valueType)
        }
    }

    override fun visit(expr: TemplateExpr): Type {
        val target = expr.target.visitValue(scope, checkNotGeneric = false)

        if (target !is TypeTemplate) {
            errorHandler("$target is not a generic template", expr.target.section)
            return target
        }

        if (target.genericParameters.size != expr.typeArguments.size) {
            errorHandler(
                "Expected ${target.genericParameters.size} type arguments, found ${expr.typeArguments.size} type arguments",
                expr.target.section
            )
            return InvalidType
        }

        var error = false
        val typeArguments = target.genericParameters.zip(expr.typeArguments) { param, arg ->
            val type = arg.resolve(scope)

            if (param.parentType != null) {
                if (!param.parentType.isAssignableFrom(type)) {
                    errorHandler("$type does not satisfy type bound ${param.parentType}", arg.section)
                    error = true
                }
            }

            param to type
        }.toMap()
        if (error) return InvalidType

        return target.template(typeArguments)
    }

    override fun visit(expr: InvokeExpr): Type {
        return checkInvocation(expr.target, expr.arguments, expr.section)
    }

    override fun visit(expr: InvokeLocalExpr): Type {
        return checkInvocation(IdentExpr(expr.name, expr.section), expr.arguments, expr.section)
    }

    override fun visit(expr: InvokeMemberExpr): Type {
        return checkInvocation(PropertyAccessExpr(expr.target, expr.name, expr.section), expr.arguments, expr.section)
    }

    override fun visit(expr: UnaryOperation): Type {
        val stmt = stmt
        val target = expr.target.visitValue(scope, stmt)

        when (expr.operator) {
            UnaryOperationType.NOT -> when (target) {
                BoolType -> {
                    val tmp = stmt.assumptions
                    stmt.assumptions = stmt.reciprocals
                    stmt.reciprocals = tmp
                    return BoolType
                }
            }
            UnaryOperationType.POSITIVE,
            UnaryOperationType.NEGATIVE -> when (target) {
                I32Type,
                I64Type,
                F64Type -> return target
            }
        }

        errorHandler("Operator ${expr.operator} is incompatible with $target", expr.section)
        return InvalidType
    }

    override fun visit(expr: BinaryOperation): Type {
        val stmt = stmt
        val left = expr.left.visitValue(scope, stmt)

        when (expr.operator) {
            BinaryOperationType.EQUALS, BinaryOperationType.NOT_EQUALS -> {
                val right = expr.right.visitValue(scope)
                if (!left.isAssignableFrom(right) && !right.isAssignableFrom(left)) {
                    errorHandler("$left and $right are incompatible", expr.section)
                } else {
                    inferTypeAssertion(
                        true,
                        expr.left,
                        left,
                        right,
                        Type::intersect,
                        Type::exclude
                    ) { key, assumption, reciprocal ->
                        stmt.putAssumption(key, assumption)
                        stmt.putReciprocal(key, reciprocal)
                    }
                    inferTypeAssertion(
                        true,
                        expr.right,
                        right,
                        left,
                        Type::intersect,
                        Type::exclude
                    ) { key, assumption, reciprocal ->
                        stmt.putAssumption(key, assumption)
                        stmt.putReciprocal(key, reciprocal)
                    }
                    if (expr.operator == BinaryOperationType.NOT_EQUALS) {
                        val tmp = stmt.assumptions
                        stmt.assumptions = stmt.reciprocals
                        stmt.reciprocals = tmp
                    }
                }

                return BoolType
            }
            BinaryOperationType.AND -> {
                val rightCtx = StmtContext().also {
                    it.assumptions.putAll(stmt.assumptions)
                }
                val right = expr.right.visitValue(scope, rightCtx)
                checkType(BoolType, left, expr.left.section)
                checkType(BoolType, right, expr.right.section)

                stmt.assumptions.mergeAll(rightCtx.assumptions, Type::intersect)

                if ((stmt.reciprocals.keys + rightCtx.reciprocals.keys).size == 1) {
                    stmt.reciprocals.mergeAll(rightCtx.reciprocals, Type::union)
                } else {
                    // cant trust any assumptions about outside if depends on multiple variables
                    stmt.reciprocals.clear()
                }
                return BoolType
            }
            BinaryOperationType.OR -> {
                val rightCtx = StmtContext().also {
                    it.assumptions.putAll(stmt.reciprocals)
                }
                val right = expr.right.visitValue(scope, rightCtx)
                checkType(BoolType, left, expr.left.section)
                checkType(BoolType, right, expr.right.section)

                stmt.reciprocals.mergeAll(rightCtx.reciprocals, Type::intersect)

                if ((stmt.assumptions.keys + rightCtx.assumptions.keys).size == 1) {
                    stmt.assumptions.mergeAll(rightCtx.assumptions, Type::union)
                } else {
                    // cant trust any reciprocals if depends on multiple variables
                    stmt.assumptions.clear()
                }
                return BoolType
            }
            else -> {
                val right = expr.right.visitValue(scope, stmt)
                when (left) {
                    StrType -> if (expr.operator == BinaryOperationType.ADD) return StrType
                    I32Type -> when (right) {
                        I32Type -> return I32Type
                        I64Type,
                        F64Type -> return right
                    }
                    I64Type -> when (right) {
                        I32Type,
                        I64Type -> return I64Type
                        F64Type -> return F64Type
                    }
                    F64Type -> when (right) {
                        I32Type,
                        I64Type,
                        F64Type -> return F64Type
                    }
                }
                errorHandler("Operator ${expr.operator} is incompatible with $left and $right", expr.section)
                return InvalidType
            }
        }
    }

    override fun visit(expr: ReturnExpr): Type {
        val expectedReturnType = scope.expectedReturnType
        if (expectedReturnType == null) {
            errorHandler("return is not valid in this context", expr.section)
        } else {
            checkType(expectedReturnType, expr.value.visitValue(scope), expr.section)
        }
        scope.terminates = true
        return NeverType
    }

    override fun visit(expr: WhileExpr): Type? {
        val conditionStmt = stmt // special test
        val condition = expr.condition.visitValue(scope, conditionStmt)

        checkType(BoolType, condition, expr.condition.section)

        val bodyScope = scope.subContext(false)
        bodyScope.assumptions += conditionStmt.assumptions

        expr.body.visitStmt(bodyScope)

        return null
    }

    override fun visit(expr: IfExpr): Type? {
        val conditionStmt = stmt // special test
        val condition = expr.condition.visitValue(scope, conditionStmt)

        checkType(BoolType, condition, expr.condition.section)

        val thenScope = scope.subContext()
        thenScope.assumptions += conditionStmt.assumptions

        val thenReturn = expr.thenBranch.visitStmt(thenScope)

        return if (expr.elseBranch != null) {
            val elseScope = scope.subContext()
            elseScope.assumptions += conditionStmt.reciprocals

            val elseConditionStmt = StmtContext() // if the else is an if
            val elseReturn = expr.elseBranch.visitStmt(elseScope, elseConditionStmt)

            if (thenScope.terminates) {
                // both scope terminates, then the whole scope terminates
                if (elseScope.terminates) scope.terminates = true
                scope.assumptions += conditionStmt.reciprocals
            } else {
                // if (some_condition) {
                //      x = a
                // } else {
                //      x = b
                // }
                // x here should be a | b
                if (expr.elseBranch is IfExpr) {
                    val outerInheritVariables = scope.allVariableNames()
                        .intersect(conditionStmt.assumptions.keys)
                        .intersect(thenScope.assumptions.keys)
                        .intersect(elseConditionStmt.assumptions.keys)
                        .intersect(elseScope.assumptions.keys)
                    outerInheritVariables.forEach { name ->
                        scope.assume(
                            name,
                            scope.getAssumption(name)!!
                                .exclude(conditionStmt.getAssumption(name)!!)
                                .union(thenScope.getAssumption(name)!!)
                                .exclude(elseConditionStmt.getAssumption(name)!!)
                                .union(elseScope.getAssumption(name)!!)

                        )
                    }
                } else {
                    println(conditionStmt.assumptions)
                    val outerInheritVariables = scope.allVariableNames()
                        .intersect(conditionStmt.assumptions.keys)
                        .intersect(thenScope.assumptions.keys)
                        .intersect(elseScope.assumptions.keys)
                    outerInheritVariables.forEach { name ->
                        scope.assume(
                            name,
                            scope.getAssumption(name)!!
                                .exclude(conditionStmt.getAssumption(name)!!)
                                .union(thenScope.getAssumption(name)!!)
                                .exclude(conditionStmt.getReciprocal(name)!!)
                                .union(elseScope.getAssumption(name)!!)

                        )
                    }
                }
            }

            return if (elseReturn != null && thenReturn != null) {
                thenReturn union elseReturn
            } else {
                null
            }
        } else {
            if (thenScope.terminates) {
                // if (x == null) return
                // x here should be NOT_NULL
                scope.assumptions += conditionStmt.reciprocals
            } else {
                // if (x == null) x = NOT_NULL
                // x here should be NOT_NULL
                val outerInheritVariables = scope.allVariableNames()
                    .intersect(conditionStmt.assumptions.keys)
                    .intersect(thenScope.assumptions.keys)
                outerInheritVariables.forEach { name ->
                    scope.assume(
                        name,
                        scope.getAssumption(name)!!
                            .exclude(conditionStmt.getAssumption(name)!!)
                            .union(thenScope.getAssumption(name)!!)
                    )
                }
            }

            // else branch is null, this is not a statement
            null
        }
    }

    override fun visit(expr: SubscriptAccessExpr): Type {
        val target = expr.target.visitValue(scope)

        val keyType = expr.index.visitValue(scope)

        return when (target) {
            is ArrayType -> {
                checkType(I32Type, keyType, expr.index.section)
                target.valueType
            }
            is MapType -> {
                checkType(target.keyType, keyType, expr.index.section)
                target.valueType
            }
            else -> InvalidType
        }
    }

    override fun visit(expr: SubscriptAssignExpr): Type? {
        val target = expr.target.visitValue(scope)

        val keyType = expr.index.visitValue(scope)
        val valueType = expr.value.visitValue(scope)

        when (target) {
            is ArrayType -> {
                checkType(I32Type, keyType, expr.index.section)
                checkType(target.valueType, valueType, expr.value.section)
            }
            is MapType -> {
                checkType(target.keyType, keyType, expr.index.section)
                checkType(target.valueType, valueType, expr.value.section)
            }
            else -> errorHandler("$target does not have subscript", expr.target.section)
        }

        return null
    }

    override fun visit(expr: PropertyAccessExpr): Type {
        val target = expr.target.visitValue(scope)
        val type = target.getMember(expr.name)?.type

        if (type == null) {
            errorHandler("$target does not have member ${expr.name}", expr.section)
        }

        return type ?: InvalidType
    }

    override fun visit(expr: PropertyAssignExpr): Type? {
        val target = expr.target.visitValue(scope)
        val member = target.getMember(expr.name)

        if (member == null) {
            errorHandler("$target does not have member ${expr.name}", expr.section)
            return null
        }

        if (member.mutable) {
            errorHandler("Member ${expr.name} of $target is not mutable", expr.section)
        }

        val valueType = expr.value.visitValue(scope)

        checkType(member.type, valueType, expr.value.section)

        return null
    }

    override fun visit(expr: TupleIndexAccessExpr): Type {
        val type = expr.target.visitValue(scope)

        if (type !is TupleType) {
            errorHandler("$type is not a tuple type", expr.target.section)
            return InvalidType
        }

        if (expr.index !in type.valueTypes.indices) {
            errorHandler(
                "$type indices only include 0..${type.valueTypes.size - 1}, tried to access ${expr.index}",
                expr.section
            )
            return InvalidType
        }

        return type.valueTypes[expr.index]
    }

    override fun visit(expr: ExternalModuleExpr): Type? {
        if (expr.name in scope.variables) {
            if (!scope.getDeclaration(expr.name)!!.mutable) {
                // if a module is mutable in this scope, it's body's typecheck is being deferred
                errorHandler("Module ${expr.name} has already been declared", expr.section)
                return null
            }
        }

        val type = ModuleType.Local(expr.name)
        val subScope = scope.subContext(false)

        scope.declare(expr.name, type, deferBody)

        expr.declarationsAST.let { declarations ->
            declarations.modules.forEach { it.visitStmt(subScope) }
            declarations.functions.forEach { it.visitStmt(subScope) }
            declarations.variables.forEach { it.visitStmt(subScope) }
        }

        type.members = subScope.variables.mapValues { (_, variable) ->
            VariableInfo(variable.type, variable.mutable)
        }.toMutableMap()

        return null
    }

    override fun visit(expr: ExternalLetExpr): Type? {
        if (expr.name in scope.variables) {
            errorHandler("Reference ${expr.name} has already been declared", expr.section)
        }

        val exprType = expr.type.resolve(scope)

        scope.declare(expr.name, exprType, expr.mutable)

        return null
    }

    override fun visit(expr: ExternalFunctionExpr): Type? {
        if (expr.name in scope.variables) {
            if (!scope.getDeclaration(expr.name)!!.mutable) {
                // if a function is mutable in this scope, it's body's typecheck is being deferred
                errorHandler("Reference ${expr.name} has already been declared", expr.section)
                return null
            }
        }

        val genericParameters = expr.generics.map { GenericParameter(it.name, it.parentType?.resolve(scope)) }
        val argumentScope = scope.subContext()

        genericParameters.forEach {
            argumentScope.putType(it.name, GenericType(it))
        }

        val arguments = expr.arguments.map { it.type.resolve(argumentScope) }

        val returnType = expr.returnType.resolve(argumentScope)

        val type = FunctionType(arguments.toList(), returnType)
        type.genericParameters = genericParameters

        scope.declare(expr.name, type, deferBody)
        return null
    }

    override fun visit(expr: MultiExpr): Type? {
        for (i in 0 until expr.list.lastIndex) {
            expr.list[i].visitStmt(scope, deferBody = deferBody)
        }
        return expr.list.last().visitStmt(scope, deferBody = deferBody)
    }

    fun Expr.visitStmt(
        scope: ScopeContext,
        stmt: StmtContext = StmtContext(),
        deferBody: Boolean = false,
        expectNotGeneric: Boolean = true,
        expectExpr: Boolean = false
    ): Type? {
        val type = accept(createSubvisitor(scope, stmt, deferBody))

        if (expectExpr && type == null) {
            errorHandler("Not a valid expression", section)
            return InvalidType
        }

        if (expectNotGeneric && type is TypeTemplate && type.genericParameters.isNotEmpty()) {
            errorHandler("Missing generic type parameters", section)
            return InvalidType
        }

        return type
    }

    fun Expr.visitValue(
        scope: ScopeContext,
        stmt: StmtContext = StmtContext(),
        deferBody: Boolean = false,
        checkNotGeneric: Boolean = true
    ): Type {
        return visitStmt(scope, stmt, deferBody, checkNotGeneric, true)!!
    }

    fun TypeAST.resolve(scope: ScopeContext): Type {
        return accept(TypeResolver(scope, errorHandler))
    }

    open fun createSubvisitor(
        scope: ScopeContext,
        stmt: StmtContext = StmtContext(),
        deferBody: Boolean = false
    ): ExprVisitor<Type?> = TypeChecker(scope, stmt, deferBody, errorHandler)

    /**
     * Throws an error if [foundType] can not be assigned to [expectedType].
     */
    fun checkType(expectedType: Type, foundType: Type, position: Section): Boolean {
        return if (!expectedType.isAssignableFrom(foundType)) {
            errorHandler("Expected $expectedType but found $foundType", position)
            false
        } else {
            true
        }
    }

    /**
     * Check that [target] is invokable by [arguments].
     * @return [target] return type.
     * @throws TypeException if [target] is not a function.
     */
    private fun checkInvocation(target: Expr, arguments: List<Expr>, position: Section): Type {
        val targetType = target.visitValue(scope)

        if (targetType !is FunctionType) {
            errorHandler("$targetType can not be invoked", target.section)
            return InvalidType
        }

        val targetArgumentTypes = targetType.argumentTypes
        val argumentTypes = arguments.map { it.visitValue(scope) }

        if (targetArgumentTypes.size != argumentTypes.size) {
            errorHandler(
                "Expected ${targetArgumentTypes.size} arguments, but found ${argumentTypes.size} arguments",
                position
            )
            return InvalidType
        }

        for (i in targetArgumentTypes.indices) {
            checkType(targetArgumentTypes[i], argumentTypes[i], arguments[i].section)
        }

        return targetType.returnType.also {
            if (it === NeverType) scope.terminates = true
        }
    }

    private inline fun inferTypeAssertion(
        unitOnly: Boolean,
        target: Expr,
        targetType: Type,
        subjectType: Type,
        function: (Type, Type) -> Type,
        inverse: (Type, Type) -> Type,
        success: (key: String, assumption: Type, reciprocal: Type) -> Unit
    ) {
        if (target !is IdentExpr) return
        if (unitOnly && !subjectType.isUnitType) return

        val assumption = function(targetType, subjectType)
        val reciprocal = inverse(targetType, subjectType)

        success(target.name, assumption, reciprocal)
    }
}

