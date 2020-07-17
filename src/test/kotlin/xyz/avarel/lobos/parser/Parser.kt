package xyz.avarel.lobos.parser

import xyz.avarel.lobos.ast.expr.Expr
import xyz.avarel.lobos.ast.expr.misc.InvalidExpr
import xyz.avarel.lobos.ast.expr.misc.MultiExpr
import xyz.avarel.lobos.ast.expr.misc.TupleExpr
import xyz.avarel.lobos.lexer.*

class Parser(val grammar: Grammar, val source: Source, val tokens: List<Token>) {
    constructor(grammar: Grammar, lexer: Tokenizer) : this(grammar, lexer.source, lexer.parse())

    val errors = mutableListOf<SyntaxException>()

    var index: Int = 0
        private set
    val eof get() = index == tokens.size
    val last get() = tokens[index - 1]

    private val precedence get() = grammar.infixParsers[peek(0).type]?.precedence ?: 0

    fun back() = tokens[--index]

//    inline fun latest(block: (Token) -> Boolean): Token {
//        var i = index
//        while (block(tokens[--i]));
//        return tokens[i]
//    }

    fun eat() = tokens[index++]

    fun eat(type: TokenType): Token {
        if (eof) throw SyntaxException("Expected $type but reached end of folder", last.section)
        val token = peek()
        if (token.type != type) {
            throw SyntaxException("Expected $type but found ${token.type}", token.section)
        }
        return eat()
    }

    fun match(type: TokenType): Boolean {
        return if (nextIs(type)) {
            eat()
            true
        } else {
            false
        }
    }

    fun matchAny(vararg type: TokenType): Boolean {
        return if (nextIsAny(*type)) {
            eat()
            true
        } else {
            false
        }
    }

    fun peek(distance: Int = 0) = tokens[index + distance]

    fun nextIs(type: TokenType) = !eof && peek().type == type

    fun nextIsAny(vararg types: TokenType) = !eof && types.any { nextIs(it) }

    fun peekAheadUntil(vararg type: TokenType): List<Token> {
        if (eof) return emptyList()
        val list = mutableListOf<Token>()
        var distance = 0
        while (!eof && !nextIsAny(*type)) {
            list += peek(distance++)
        }
        return list
    }

    fun skipUntil(vararg type: TokenType) {
        while (!eof && !nextIsAny(*type)) {
            eat()
        }
    }

    fun parse(): Expr {
        if (eof) return TupleExpr(Section(source, 0, 0, 0))
        val expr = parseStatements()

        if (!eof) {
            val token = peek()
            errors += SyntaxException("Did not reach end of folder. Found token $token", token.section)
        }

        return expr
    }

    /** [block] returns false if it wants to skip to the next statement */
    inline fun delimitedBlock(delimiterPair: Pair<TokenType, TokenType>? = null, block: () -> Boolean) {
        if (eof) {
            if (tokens.isEmpty()) {
                throw SyntaxException("Expected block but reached end of file", Section(source, 0, 0, 0))
            } else {
                throw SyntaxException("Expected block but reached end of file", last.section)
            }
        }

        delimiterPair?.first?.let(this::eat)
        matchAll(TokenType.NL, TokenType.SEMICOLON)

        do {
            if (eof || (delimiterPair != null && nextIs(delimiterPair.second))) {
                break
            }
            if (!block()) { //returns true to skip to the next statement
                if (delimiterPair != null) {
                    skipUntil(delimiterPair.second, TokenType.SEMICOLON, TokenType.NL)
                } else {
                    skipUntil(TokenType.SEMICOLON, TokenType.NL)
                }
            }
        } while (!eof && matchAll(TokenType.NL, TokenType.SEMICOLON))

        delimiterPair?.second?.let(this::eat)
    }

    fun parseStatements(
        delimiterPair: Pair<TokenType, TokenType>? = null,
        modifiers: List<Modifier> = emptyList()
    ): Expr {
        if (eof) throw SyntaxException("Expected block but reached end of folder", last.section)

        val list = mutableListOf<Expr>()

        delimitedBlock(delimiterPair) {
            val expr = parseExpr(0, modifiers)

            if (expr is InvalidExpr) {
                false
            } else {
                list += expr
                true
            }
        }

        if (last.type == TokenType.SEMICOLON) list += TupleExpr(last.section)

        return when {
            list.isEmpty() -> TupleExpr(last.section)
            list.size == 1 -> list[0]
            else -> MultiExpr(list)
        }
    }

    fun parseExpr(
        precedence: Int = 0,
        modifiers: List<Modifier> = emptyList()
    ): Expr {
        if (eof) throw SyntaxException("Expected expression but reached end of folder", last.section)

        val token = eat()

        val parser = grammar.prefixParsers[token.type] ?: let {
            errors += SyntaxException("Unexpected ${token.type}", token.section)
            return InvalidExpr(token.section)
        }

        val expr = try {
            parser.parse(this, modifiers, token)
        } catch (e: SyntaxException) {
            errors += e
            return InvalidExpr(e.position)
        }

        return parseInfix(precedence, expr)
    }

    fun parseInfix(precedence: Int, left: Expr): Expr {
        var leftExpr = left
        while (!eof && precedence < this.precedence) {
            val token = eat()
            val parser = grammar.infixParsers[token.type] ?: let {
                errors += SyntaxException("Unexpected $token", token.section)
                return InvalidExpr(token.section)
            }

            leftExpr = try {
                parser.parse(this, token, leftExpr)
            } catch (e: SyntaxException) {
                errors += e
                return InvalidExpr(e.position)
            }
        }
        return leftExpr
    }
}