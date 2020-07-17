package xyz.avarel.lobos.tc

import xyz.avarel.lobos.ast.patterns.*
import xyz.avarel.lobos.tc.base.I32Type
import xyz.avarel.lobos.tc.base.StrType
import xyz.avarel.lobos.tc.complex.TupleType
import xyz.avarel.lobos.tc.scope.ScopeContext

class PatternTypeBinder(
    val tc: TypeChecker,
    val targetType: Type,
    val scope: ScopeContext,
    val fromLet: Boolean = false
) : PatternVisitor<Type?> {
    override fun visit(pattern: WildcardPattern): Type? {
        return targetType
    }

    override fun visit(pattern: I32Pattern): Type? {
        return if (tc.checkType(targetType, I32Type, pattern.section)) {
            targetType
        } else {
            null
        }
    }

    override fun visit(pattern: StrPattern): Type? {
        return if (tc.checkType(targetType, StrType, pattern.section)) {
            targetType
        } else {
            null
        }
    }

    override fun visit(pattern: TuplePattern): Type? {
        return when {
            targetType !is TupleType -> {
                tc.errorHandler("$targetType can not be matched to a tuple pattern", pattern.section)
                null
            }
            targetType.valueTypes.size != pattern.list.size -> {
                tc.errorHandler(
                    "Tuple pattern size mismatch, expected ${pattern.list.size}, found ${targetType.valueTypes.size}",
                    pattern.section
                )
                null
            }
            pattern.list.zip(targetType.valueTypes)
                .map { (pattern, type) -> pattern.accept(PatternTypeBinder(tc, type, scope)) }
                .all { it != null } -> targetType
            else -> null
        }
    }

    override fun visit(pattern: VariablePattern): Type? {
        if (fromLet && pattern.name in scope.variables) {
            tc.errorHandler("Reference ${pattern.name} has already been declared", pattern.section)
            return null
        }

        if (pattern.type != null) {
            val type = tc.run { pattern.type.resolve(scope) }

            scope.declare(pattern.name, type, pattern.mutable)

            if (type.isAssignableFrom(targetType)) {
                scope.assume(pattern.display, targetType)
            } else {
                tc.errorHandler("Expected $type but found $targetType", pattern.section)
            }

            return type
        } else {
            scope.declare(pattern.name, targetType.universalType, pattern.mutable)
        }

        return targetType
    }
}