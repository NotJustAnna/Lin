package io.github.cafeteriaguild.lin.parser.utils

import com.github.adriantodt.tartar.api.parser.ParserContext

fun <T, E> ParserContext<T, E>.matchAll(vararg types: T): Boolean {
    return if (nextIsAny(*types)) {
        do eat() while (nextIsAny(*types))
        true
    } else {
        false
    }
}
