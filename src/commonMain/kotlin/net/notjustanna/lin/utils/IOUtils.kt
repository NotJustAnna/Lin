package net.notjustanna.lin.utils

import okio.Buffer

internal fun Buffer.readU24(): Int {
    return readByte().toInt() shl 16 or readShort().toInt()
}

internal fun Buffer.writeU24(value: Int) = writeByte(value ushr 16).writeShort(value)

internal fun Buffer.readU12Pair(): Pair<Int, Int> {
    return readU24().let { (it shl 12) to (it and 0xFFF) }
}

internal fun Buffer.writeU12Pair(first: Int, second: Int) {
    writeU24((first ushr 12) or (second and 0xFFF))
}

internal fun Buffer.skipByte(): Buffer = apply { readByte() }
