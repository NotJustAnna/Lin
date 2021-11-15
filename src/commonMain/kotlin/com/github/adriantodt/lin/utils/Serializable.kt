package com.github.adriantodt.lin.utils

import okio.Buffer
import okio.ByteString

public interface Serializable {
    public fun serializeTo(buffer: Buffer)

    public fun toBytes(): ByteString {
        val buffer = Buffer()
        serializeTo(buffer)
        return buffer.snapshot()
    }
}
