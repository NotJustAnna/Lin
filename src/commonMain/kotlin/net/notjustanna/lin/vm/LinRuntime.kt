package net.notjustanna.lin.vm

import net.notjustanna.lin.vm.impl.Exceptions
import net.notjustanna.lin.vm.types.*

object LinRuntime {
    val ensureNotNull = LNativeFunction { _, args ->
        if (args.any { it == LNull }) {
            throw LAnyException(Exceptions.nullPointer())
        }
        LTrue
    }

    val iterator = LNativeFunction { thisValue, _ ->
        val it = when (thisValue) {
            is LArray -> {
                thisValue.value.iterator()
            }
            is LObject -> {
                thisValue.value.asSequence().map(LAny::ofEntry).iterator()
            }
            is LRange -> {
                thisValue.value.asSequence().map(::LInteger).iterator()
            }
            null -> {
                throw LAnyException(Exceptions.nullPointer())
            }
            else -> {
                throw LAnyException(Exceptions.unsupportedOperation("iterator", thisValue.linType))
            }
        }
        LObject.of(
            LString("__hasNext") to LNativeFunction { _, _ -> LAny.of(it.hasNext()) },
            LString("__next") to LNativeFunction { _, _ -> it.next() },
        )
    }
}
