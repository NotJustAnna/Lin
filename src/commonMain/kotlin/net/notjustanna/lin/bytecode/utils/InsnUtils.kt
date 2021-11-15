package net.notjustanna.lin.bytecode.utils

import net.notjustanna.lin.exception.IntegerOutOfBoundsException
import okio.Buffer

private const val bit24th = 0x800000
private const val i24Mask = -0x1000000
internal const val maxU24 = 0xFFFFFF

internal const val maxI24 = 0x7FFFFF
internal const val minI24 = -0x800000

internal inline fun Int.requireU24(field: String): Int {
    if (this !in 0..maxU24) {
        throw IntegerOutOfBoundsException("Field $field is outside of U24 bounds ($this !in 0..0xFFFFFF)")
    }
    return this
}

internal inline fun Int.requireI24(field: String): Int {
    if (this !in minI24..maxI24) {
        throw IntegerOutOfBoundsException("Field $field is outside of I24 bounds ($this !in -0x800000..0x7FFFFF)")
    }
    return this
}

internal inline fun Int.requireU16(field: String): Int {
    if (this !in 0..0xFFFF) {
        throw IntegerOutOfBoundsException("Field $field is outside of U16 bounds ($this !in 0..0xFFFF)")
    }
    return this
}

internal inline fun Int.requireU12(field: String): Int {
    if (this !in 0..0xFFF) {
        throw IntegerOutOfBoundsException("Field $field is outside of U12 bounds ($this !in 0..0xFFF)")
    }
    return this
}

internal inline fun Int.requireU8(field: String): Int {
    if (this !in 0..0xFF) {
        throw IntegerOutOfBoundsException("Field $field is outside of U8 bounds ($this !in 0..0xFF)")
    }
    return this
}

internal inline fun Buffer.readU24(): Int {
    return readU8() shl 16 or readU16()
}

internal inline fun Buffer.readU8(): Int {
    return readByte().toInt() and 0xFF
}

// internal fun Buffer.readNU8(): Int {
//     val u = readU8()
//     return if (u == 0xFF) -1 else u
// }

internal inline fun Buffer.readU16(): Int {
    return readShort().toInt() and 0xFFFF
}

internal inline fun Buffer.readI24(): Int {
    val u = readU24()
    return if (u and bit24th == bit24th) u or i24Mask else u
}

// internal inline fun Buffer.readNU24(): Int {
//     val u = readU24()
//     return if (u == maxU24) -1 else u
// }

internal inline fun Buffer.writeU24(value: Int) = writeByte(value ushr 16).writeShort(value)

internal inline fun Buffer.readU12Pair(): Pair<Int, Int> {
    return readU24().let { (it shl 12 and 0xFFF) to (it and 0xFFF) }
}

internal inline fun Buffer.writeU12Pair(first: Int, second: Int) {
    writeU24((first ushr 12) or (second and 0xFFF))
}

internal inline fun Buffer.skipByte(): Buffer = apply { readByte() }
