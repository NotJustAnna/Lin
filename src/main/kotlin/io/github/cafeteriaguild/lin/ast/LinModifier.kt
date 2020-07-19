package io.github.cafeteriaguild.lin.ast

import net.notjustanna.tartar.api.parser.Token

enum class LinModifier {
    ABSTRACT, ANNOTATION, COMPANION, CONST, DATA, ENUM,
    FINAL, INFIX, INTERNAL, LATEINIT, OPEN, OPERATOR, OVERRIDE,
    PRIVATE, PROTECTED, PUBLIC, SEALED;

    companion object {
        private val map by lazy { values().associateBy { it.name.toLowerCase() } }
        val names by lazy { map.keys }
        fun <T> parse(list: List<Token<T>>) = list.mapNotNull { t -> map[t.value]?.let { it to t } }.toMap()

        private val conflicting = listOf(
            listOf(PUBLIC, PROTECTED, PRIVATE, INTERNAL),
            listOf(DATA, ENUM, ANNOTATION, ABSTRACT),
            listOf(DATA, ENUM, ANNOTATION, SEALED),
            listOf(DATA, ENUM, ANNOTATION, OPEN),
            listOf(ABSTRACT, FINAL),
            listOf(ABSTRACT, LATEINIT, CONST),
            listOf(OPEN, FINAL)
        )
    }
}