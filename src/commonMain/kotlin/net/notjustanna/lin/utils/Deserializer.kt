package net.notjustanna.lin.utils

import okio.Buffer
import okio.ByteString
import okio.use

public interface Deserializer<T> {
    public fun deserializeFrom(buffer: Buffer): T

    public fun fromBytes(source: ByteString): T {
        Buffer().use {
            it.write(source)
            return deserializeFrom(it)
        }
    }
}
