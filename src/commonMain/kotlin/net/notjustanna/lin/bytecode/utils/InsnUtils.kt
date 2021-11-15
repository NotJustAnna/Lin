package net.notjustanna.lin.bytecode.utils

import okio.Buffer

private const val bit24th = 0x800000
private const val i24Mask: Int = -0x1000000
const val maxU24 = 0xFFFFFF

internal fun Buffer.readU24(): Int {
    return (readByte().toInt() and 0xFF shl 16) or (readShort().toInt() and 0xFFFF)
}

internal fun Buffer.readI24(): Int {
    val u = readU24()
    return if (u and bit24th == bit24th) u or i24Mask else u
}

internal fun Buffer.readNU24(): Int {
    val u = readU24()
    return if (u == maxU24) -1 else u
}

internal fun Buffer.writeU24(value: Int) = writeByte(value ushr 16).writeShort(value)

internal fun Buffer.readU12Pair(): Pair<Int, Int> {
    return readU24().let { (it shl 12) to (it and 0xFFF) }
}

internal fun Buffer.writeU12Pair(first: Int, second: Int) {
    writeU24((first ushr 12) or (second and 0xFFF))
}

internal fun Buffer.skipByte(): Buffer = apply { readByte() }
