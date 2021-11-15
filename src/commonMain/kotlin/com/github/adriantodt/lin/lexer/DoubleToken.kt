package com.github.adriantodt.lin.lexer

import com.github.adriantodt.tartar.api.lexer.Section
import com.github.adriantodt.tartar.api.parser.Token

public class DoubleToken<T>(type: T, public val value: Double, section: Section? = null) : Token<T>(type, section) {
    /**
     * Returns a string representation of the token.
     */
    override fun toString(): String {
        return "$type[$value] $section"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false
        if (!super.equals(other)) return false

        other as DoubleToken<*>

        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int {
        return 31 * super.hashCode() + value.hashCode()
    }
}
