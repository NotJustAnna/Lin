package net.notjustanna.lin.bytecode

import net.notjustanna.lin.utils.Serializable
import okio.Buffer
import okio.ByteString.Companion.encodeUtf8

data class CompiledSource(
    val intPool: List<Int>,
    val longPool: List<Long>,
    val floatPool: List<Float>,
    val doublePool: List<Double>,
    val stringPool: List<String>,
    val functionParameters: List<List<CompiledParameter>>,
    val functions: List<CompiledFunction>,
    val nodes: List<CompiledNode>
) : Serializable {
    override fun serializeTo(buffer: Buffer) {
        buffer.writeInt(intPool.size)
        for (i in intPool) buffer.writeInt(i)

        buffer.writeInt(longPool.size)
        for (l in longPool) buffer.writeLong(l)

        buffer.writeInt(floatPool.size)
        for (f in floatPool) buffer.writeInt(f.toBits())

        buffer.writeInt(doublePool.size)
        for (d in doublePool) buffer.writeLong(d.toBits())

        buffer.writeInt(stringPool.size)
        for (s in stringPool) buffer.write(s.encodeUtf8())

        buffer.writeInt(functionParameters.size)
        for (list in functionParameters) {
            buffer.writeInt(list.size)
            for (parameter in list) parameter.serializeTo(buffer)
        }

        buffer.writeInt(functions.size)
        for (function in functions) function.serializeTo(buffer)

        buffer.writeInt(nodes.size)
        for (node in nodes) node.serializeTo(buffer)
    }

}
