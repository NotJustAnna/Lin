package com.github.adriantodt.lin.js

import com.github.adriantodt.lin.Lin
import com.github.adriantodt.lin.compiler.NodeCompiler
import com.github.adriantodt.tartar.api.lexer.Source
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

@Suppress("unused")
@OptIn(ExperimentalTime::class, ExperimentalJsExport::class)
@JsExport
object Lin {
    fun compile(source: String): Compilation {
        val (node, parseDuration) = measureTimedValue { Lin.parser.parse(Source(source, "snippet.lin")) }
        val (compiledSource, compileDuration) = measureTimedValue { NodeCompiler.compile(node) }
        return Compilation(compiledSource, parseDuration.toString(), compileDuration.toString())
    }
}
