package com.github.adriantodt.lin.utils

import okio.Buffer
import okio.ByteString

interface Serializable {
    fun serializeTo(buffer: Buffer)

    fun toBytes(): ByteString {
        val buffer = Buffer()
        serializeTo(buffer)
        return buffer.snapshot()
    }
}
