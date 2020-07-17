package xyz.avarel.lobos.parser.parselets

import xyz.avarel.lobos.parser.InfixParser

abstract class BinaryParser(override val precedence: Int, val leftAssoc: Boolean = true) : InfixParser