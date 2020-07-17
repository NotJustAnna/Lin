package xyz.avarel.lobos.lexer

interface Sectional {
    val section: Section
    fun span(other: Sectional) = section.span(other.section)
}