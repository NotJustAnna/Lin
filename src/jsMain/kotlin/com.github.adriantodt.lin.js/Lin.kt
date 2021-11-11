package com.github.adriantodt.lin.js

import com.github.adriantodt.lin.Lin
import com.github.adriantodt.tartar.api.lexer.Source
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

@Suppress("unused")
@OptIn(ExperimentalTime::class, ExperimentalJsExport::class)
@JsExport
object Lin {
    fun parse(source: String, name: String = "snippet.lin"): ParseResult {
        return ParseResult(measureTimedValue { Lin.parser.runCatching { parse(Source(source, name)) } })
    }
}
