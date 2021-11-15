package com.github.adriantodt.lin.bytecode.utils

import com.github.adriantodt.lin.exception.IntegerOutOfBoundsException
import okio.Buffer

private const val bit24th = 0x800000
private const val i24Mask: Int = -0x1000000
const val maxU24 = 0xFFFFFF

const val maxI24 = 0x7FFFFF
const val minI24 = -0x800000

internal fun Int.requireU24(field: String): Int {
    if (this !in 0..maxU24) {
        throw IntegerOutOfBoundsException("Field $field is outside of U24 bounds ($this !in 0..0xFFFFFF)")
    }
    return this
}

internal fun Int.requireI24(field: String): Int {
    if (this !in minI24..maxI24) {
        throw IntegerOutOfBoundsException("Field $field is outside of I24 bounds ($this !in -0x800000..0x7FFFFF)")
    }
    return this
}

internal fun Int.requireU16(field: String): Int {
    if (this !in 0..0xFFFF) {
        throw IntegerOutOfBoundsException("Field $field is outside of U16 bounds ($this !in 0..0xFFFF)")
    }
    return this
}

internal fun Int.requireU12(field: String): Int {
    if (this !in 0..0xFFF) {
        throw IntegerOutOfBoundsException("Field $field is outside of U12 bounds ($this !in 0..0xFFF)")
    }
    return this
}

internal fun Int.requireU8(field: String): Int {
    if (this !in 0..0xFF) {
        throw IntegerOutOfBoundsException("Field $field is outside of U8 bounds ($this !in 0..0xFF)")
    }
    return this
}

internal fun Buffer.readU24(): Int {
    return readU8() shl 16 or readU16()
}

internal fun Buffer.readU8(): Int {
    return readByte().toInt() and 0xFF
}

// internal fun Buffer.readNU8(): Int {
//     val u = readU8()
//     return if (u == 0xFF) -1 else u
// }

internal fun Buffer.readU16(): Int {
    return readShort().toInt() and 0xFFFF
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
    return readU24().let { (it shl 12 and 0xFFF) to (it and 0xFFF) }
}

internal fun Buffer.writeU12Pair(first: Int, second: Int) {
    writeU24((first ushr 12) or (second and 0xFFF))
}

internal fun Buffer.skipByte(): Buffer = apply { readByte() }
