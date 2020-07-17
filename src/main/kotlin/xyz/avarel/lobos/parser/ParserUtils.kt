package xyz.avarel.lobos.parser

import xyz.avarel.lobos.ast.DeclarationsAST
import xyz.avarel.lobos.ast.ExternalDeclarationsAST
import xyz.avarel.lobos.ast.expr.Expr
import xyz.avarel.lobos.ast.expr.declarations.*
import xyz.avarel.lobos.ast.patterns.*
import xyz.avarel.lobos.ast.types.GenericParameterAST
import xyz.avarel.lobos.ast.types.TypeAST
import xyz.avarel.lobos.ast.types.basic.IdentTypeAST
import xyz.avarel.lobos.ast.types.basic.NeverTypeAST
import xyz.avarel.lobos.ast.types.basic.NullTypeAST
import xyz.avarel.lobos.ast.types.complex.*
import xyz.avarel.lobos.lexer.Section
import xyz.avarel.lobos.lexer.TokenType

val declarationTokens = listOf(
    TokenType.MOD,
    TokenType.LET,
    TokenType.DEF,
    TokenType.EXTERNAL,
    TokenType.TYPE
)

//fun Parser.matchAllWhitespace(): Boolean {
//    return matchAll(TokenType.NL)
//}


fun Parser.parsePattern(): PatternAST {
    return when {
        match(TokenType.UNDERSCORE) -> WildcardPattern(last.section)
        match(TokenType.INT) -> I32Pattern(last.string.toInt(), last.section)
        match(TokenType.STRING) -> StrPattern(last.string, last.section)
        match(TokenType.L_PAREN) -> {
            val lParen = last.section
            val list = mutableListOf<PatternAST>()
            if (!match(TokenType.R_PAREN)) {
                do {
                    list += parsePattern()
                } while (match(TokenType.COMMA))
                eat(TokenType.R_PAREN)
            }
            TuplePattern(list, lParen.span(last.section))
        }
        match(TokenType.IDENT) -> {
            val name = last.string
            val type = if (match(TokenType.COLON)) parseTypeAST() else null
            VariablePattern(false, name, type, last.section)
        }
        match(TokenType.MUT) -> {
            val name = last.string
            val type = if (match(TokenType.COLON)) parseTypeAST() else null
            val mutToken = last
            VariablePattern(false, name, type, mutToken.span(last))
        }
        else -> throw SyntaxException("Expected pattern", peek().section)
    }
}

fun Parser.matchAll(vararg types: TokenType): Boolean {
    return if (nextIsAny(*types)) {
        do eat() while (nextIsAny(*types))
        true
    } else {
        false
    }
}

fun Parser.parseDeclarations(delimiterPair: Pair<TokenType, TokenType>? = TokenType.L_BRACE to TokenType.R_BRACE): DeclarationsAST {
    val declarationsAST = DeclarationsAST()

    if (eof) return declarationsAST

    delimitedBlock(delimiterPair) {
        if (peek().type !in declarationTokens) {
            errors += SyntaxException("Only allow $declarationTokens in this context", peek().section)
            false
        } else {
            val expr = parseExpr()

            when (expr) {
                is UseExpr -> declarationsAST.uses += expr
                is FunctionExpr -> declarationsAST.functions += expr
                is LetExpr -> declarationsAST.variables += expr
                is ModuleExpr -> declarationsAST.modules += expr
                else -> {
                }
            }

            true
        }
    }
    return declarationsAST
}

fun Parser.parseExternalDeclarations(): ExternalDeclarationsAST {
    val declarationsAST = ExternalDeclarationsAST()

    delimitedBlock(TokenType.L_BRACE to TokenType.R_BRACE) {
        if (peek().type !in declarationTokens) {
            errors += SyntaxException("Only allow $declarationTokens in this context", peek().section)
            false
        } else {
            val expr = parseExpr(0, listOf(Modifier.EXTERNAL))

            when (expr) {
                is UseExpr -> declarationsAST.uses += expr
                is ExternalFunctionExpr -> declarationsAST.functions += expr
                is ExternalLetExpr -> declarationsAST.variables += expr
                is ExternalModuleExpr -> declarationsAST.modules += expr
                else -> throw IllegalStateException()
            }

            true
        }
    }
    return declarationsAST
}

fun Parser.parseBlock(
    modifiers: List<Modifier> = emptyList()
): Expr {
    return parseStatements(TokenType.L_BRACE to TokenType.R_BRACE, modifiers)
}

fun Parser.parseTypeAST(): TypeAST {
    val type = parseSingleTypeAST()

    if (match(TokenType.PIPE)) {
        val list = mutableListOf(type)
        do {
            list += parseSingleTypeAST()
        } while (match(TokenType.PIPE))
        return list.reduce { acc, typeAST -> UnionTypeAST(acc, typeAST, acc.section.span(typeAST.section)) }
    }

    return type
}

fun Parser.parseSingleTypeAST(): TypeAST {
    return when {
        match(TokenType.NULL) -> NullTypeAST(last.section)
        match(TokenType.BANG) -> NeverTypeAST(last.section)
        match(TokenType.IDENT) -> {
            val ident = last
            val name = ident.string

            val type = IdentTypeAST(name, ident.section)

            if (!match(TokenType.LT)) return type

            val typeParameters = mutableListOf<TypeAST>()
            do {
                typeParameters += parseTypeAST()
            } while (match(TokenType.COMMA))
            val gt = eat(TokenType.GT)

            return TemplatingTypeAST(type, typeParameters, ident.section.span(gt.section))
        }
        match(TokenType.L_PAREN) -> {
            val lParen = last
            val valueTypes = mutableListOf<TypeAST>()

            if (!match(TokenType.R_PAREN)) {
                val firstType = parseTypeAST()
                valueTypes += firstType

                if (match(TokenType.R_PAREN)) {
                    if (nextIs(TokenType.ARROW)) {
                        return constructTupleOrFunctionType(valueTypes, lParen.section.span(last.section))
                    }

                    return firstType
                }

                if (match(TokenType.COMMA)) {
                    if (match(TokenType.R_PAREN)) {
                        return TupleTypeAST(listOf(firstType), lParen.section.span(last.section))
                    }
                    do {
                        valueTypes += parseTypeAST()
                    } while (match(TokenType.COMMA))
                }

                eat(TokenType.R_PAREN)
            }

            return constructTupleOrFunctionType(valueTypes, lParen.section.span(last.section))
        }
        match(TokenType.L_BRACKET) -> {
            val lBracket = last

            val type = parseTypeAST()

            if (match(TokenType.COLON)) {
                val valueType = parseTypeAST()
                val rBracket = eat(TokenType.R_BRACKET)
                return MapTypeAst(type, valueType, lBracket.span(rBracket))
            }

            val rBracket = eat(TokenType.R_BRACKET)
            return ArrayTypeAST(type, lBracket.span(rBracket))
        }
        else -> throw SyntaxException("Expected type", peek().section)
    }
}

private fun Parser.constructTupleOrFunctionType(valueTypes: List<TypeAST>, position: Section): TypeAST {
    return when {
        match(TokenType.ARROW) -> {
            val returnType = parseTypeAST()
            FunctionTypeAST(valueTypes, returnType, position)
        }
        valueTypes.isEmpty() -> TupleTypeAST(position)
        else -> TupleTypeAST(valueTypes, position)
    }
}

fun Parser.parseGenericParameters(): List<GenericParameterAST> {
    if (!match(TokenType.LT)) return emptyList()

    val genericNames = mutableListOf<String>()
    val genericParameters = mutableListOf<GenericParameterAST>()
    do {
        val genericToken = eat(TokenType.IDENT)
        val genericName = genericToken.string

        if (genericName in genericNames) {
            errors += SyntaxException("Generic parameter $genericName has already been declared", genericToken.section)
        }

        val param = if (match(TokenType.COLON)) {
            val parentType = parseTypeAST()
            GenericParameterAST(genericName, parentType)
        } else {
            GenericParameterAST(genericName)
        }

        genericParameters += param
    } while (match(TokenType.COMMA))

    eat(TokenType.GT)
    return genericParameters
}

inline fun <K, V> MutableMap<K, V>.mergeAll(other: Map<K, V>, remappingFunction: (V, V) -> V) {
    other.forEach { (k, v) ->
        this[k]?.let {
            put(k, remappingFunction(it, v))
        } ?: put(k, v)
    }
}

inline fun <R> Parser.safe(block: () -> R): R? {
    return try {
        block()
    } catch (e: SyntaxException) {
        this.errors += e
        null
    }
}