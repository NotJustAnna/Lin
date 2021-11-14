package com.github.adriantodt.lin.parser.utils

import com.github.adriantodt.tartar.api.parser.ParserContext
import com.github.adriantodt.tartar.api.parser.Token

fun <T, E> ParserContext<T, Token<T>, E>.matchAll(vararg types: T): Boolean {
    return if (nextIsAny(*types)) {
        do eat() while (nextIsAny(*types))
        true
    } else {
        false
    }
}
