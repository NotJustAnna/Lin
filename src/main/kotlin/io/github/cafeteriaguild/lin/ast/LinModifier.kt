package io.github.cafeteriaguild.lin.ast

import net.notjustanna.tartar.api.parser.Token

enum class LinModifier {
    ACTUAL, ABSTRACT, ANNOTATION, COMPANION, CONST, DATA, ENUM,
    FINAL, INFIX, INTERNAL, LATEINIT, OPEN, OPERATOR, OVERRIDE,
    PRIVATE, PROTECTED, PUBLIC, SEALED, VARARG;

    companion object {
        private val map by lazy { values().associateBy { it.name.toLowerCase() } }
        val names by lazy { map.keys }
        fun <T> parse(list: List<Token<T>>) = list.mapNotNull { t -> map[t.value]?.let { it to t } }.toMap()
    }
}