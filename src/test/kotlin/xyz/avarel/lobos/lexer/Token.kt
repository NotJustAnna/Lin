package xyz.avarel.lobos.lexer

data class Token(val type: TokenType, val string: String, override val section: Section) : Sectional {
    override fun toString() = "$type[$string] $section"
}