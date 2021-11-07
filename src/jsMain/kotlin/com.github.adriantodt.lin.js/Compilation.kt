package com.github.adriantodt.lin.js

import com.github.adriantodt.lin.bytecode.CompiledSource

@Suppress("unused")
@OptIn(ExperimentalJsExport::class)
@JsExport
class Compilation internal constructor(
    private val compiledSource: CompiledSource,
    val parseDuration: String,
    val compileDuration: String
) {
    fun sourceToBytes(): ByteArray {
        return compiledSource.toBytes().toByteArray()
    }

    fun sourceToHex(): String {
        return compiledSource.toBytes().hex()
    }

    fun sourceToBase64(): String {
        return compiledSource.toBytes().base64()
    }

    fun createVM(): VirtualMachine {
        return VirtualMachine(compiledSource)
    }
}
