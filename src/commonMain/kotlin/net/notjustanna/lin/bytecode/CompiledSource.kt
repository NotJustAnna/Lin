package net.notjustanna.lin.bytecode

import net.notjustanna.lin.bytecode.utils.maxU24
import net.notjustanna.lin.bytecode.utils.readU24
import net.notjustanna.lin.bytecode.utils.writeU24
import net.notjustanna.lin.exception.IllegalConstantIndexException
import net.notjustanna.lin.utils.Deserializer
import net.notjustanna.lin.utils.Serializable
import okio.Buffer
import okio.ByteString.Companion.encodeUtf8

data class CompiledSource(
    val longPool: List<Long>,
    val stringPool: List<String>,
    val functionParameters: List<List<CompiledParameter>>,
    val functions: List<CompiledFunction>,
    val sections: List<CompiledSection>,
    val nodes: List<CompiledNode>
) : Serializable {
    override fun serializeTo(buffer: Buffer) {
        check(longPool.size < maxU24) {
            "Compiled Source cannot be serialized as the long pool exceeds the maximum size (${longPool.size} >= $maxU24)"
        }
        buffer.writeU24(longPool.size)
        for (l in longPool) buffer.writeLong(l)

        check(stringPool.size < maxU24) {
            "Compiled Source cannot be serialized as the string pool exceeds the maximum size (${stringPool.size} >= $maxU24)"
        }
        buffer.writeU24(stringPool.size)
        for (s in stringPool) {
            val encoded = s.encodeUtf8()
            buffer.writeInt(encoded.size).write(encoded)
        }

        check(functionParameters.size < maxU24) {
            "Compiled Source cannot be serialized as the function parameter definitions exceeds the maximum size (${functionParameters.size} >= $maxU24)"
        }
        buffer.writeU24(functionParameters.size)
        for (list in functionParameters) {
            check(list.size < 256) {
                "Compiled Source cannot be serialized as a function parameter definition exceeded the maximum size (${list.size} > 255)"
            }
            buffer.writeByte(list.size)
            for (parameter in list) parameter.serializeTo(buffer)
        }

        check(functions.size < maxU24) {
            "Compiled Source cannot be serialized as the function definitions exceeds the maximum size (${functions.size} >= $maxU24)"
        }
        buffer.writeU24(functions.size)
        for (function in functions) function.serializeTo(buffer)

        buffer.writeInt(sections.size)
        for (section in sections) section.serializeTo(buffer)

        buffer.writeInt(nodes.size)
        for (node in nodes) node.serializeTo(buffer)
    }

    fun longConstOrNull(index: Int): Long? {
        return longPool.getOrNull(index)
    }

    fun longConst(index: Int): Long {
        return longConstOrNull(index) ?: throw IllegalConstantIndexException(index)
    }

    fun stringConstOrNull(index: Int): String? {
        return stringPool.getOrNull(index)
    }

    fun stringConst(index: Int): String {
        return stringConstOrNull(index) ?: throw IllegalConstantIndexException(index)
    }

    companion object : Deserializer<CompiledSource> {
        override fun deserializeFrom(buffer: Buffer): CompiledSource {
            val longPool = mutableListOf<Long>()
            repeat(buffer.readU24()) {
                longPool += buffer.readLong()
            }

            val stringPool = mutableListOf<String>()
            repeat(buffer.readU24()) {
                val size = buffer.readInt()
                stringPool += buffer.readByteString(size.toLong()).utf8()
            }

            val functionParameters = mutableListOf<List<CompiledParameter>>()
            repeat(buffer.readU24()) {
                val list = mutableListOf<CompiledParameter>()
                repeat(buffer.readByte().toInt() and 0xFF) {
                    list += CompiledParameter.deserializeFrom(buffer)
                }
                functionParameters += list
            }

            val functions = mutableListOf<CompiledFunction>()
            repeat(buffer.readU24()) {
                functions += CompiledFunction.deserializeFrom(buffer)
            }

            val sections = mutableListOf<CompiledSection>()
            repeat(buffer.readInt()) {
                sections += CompiledSection.deserializeFrom(buffer)
            }

            val nodes = mutableListOf<CompiledNode>()
            repeat(buffer.readInt()) {
                nodes += CompiledNode.deserializeFrom(buffer)
            }

            return CompiledSource(longPool, stringPool, functionParameters, functions, sections, nodes)
        }
    }
}
