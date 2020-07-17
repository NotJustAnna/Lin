package xyz.avarel.lobos.parser

import xyz.avarel.lobos.lexer.Section

open class SyntaxException(message: String, val position: Section) : RuntimeException("$message at $position")

class TypeException(message: String, position: Section) : SyntaxException(message, position)