package net.notjustanna.lin.lexer

import net.notjustanna.tartar.api.lexer.Section
import net.notjustanna.tartar.api.parser.Token

class LongToken<T>(type: T, val value: Long, section: Section? = null) : Token<T>(type, section) {
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

        other as LongToken<*>

        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int {
        return 31 * super.hashCode() + value.hashCode()
    }
}
