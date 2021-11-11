package net.notjustanna.lin.js

import net.notjustanna.lin.Lin
import net.notjustanna.tartar.api.lexer.Source
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
