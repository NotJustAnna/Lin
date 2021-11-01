package com.github.adriantodt.lin.utils

import okio.Buffer
import okio.ByteString
import okio.use

interface Deserializer<T> {
    fun deserializeFrom(buffer: Buffer) : T

    fun fromBytes(source: ByteString): T {
        Buffer().use {
            it.write(source)
            return deserializeFrom(it)
        }
    }
}
