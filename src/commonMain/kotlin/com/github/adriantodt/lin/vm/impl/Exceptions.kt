package com.github.adriantodt.lin.vm.impl

import com.github.adriantodt.lin.exception.LinNativeException
import com.github.adriantodt.lin.vm.StackTrace
import com.github.adriantodt.lin.vm.types.LAny
import com.github.adriantodt.lin.vm.types.LArray
import com.github.adriantodt.lin.vm.types.LObject
import com.github.adriantodt.lin.vm.types.LString

object Exceptions {
    // TODO Add support for scope/lines

    fun create(type: String, description: String, stackTrace: List<StackTrace>): LObject {
        return LObject(
            LString("errorType") to LString(type),
            LString("description") to LString(description),
            LString("stackTrace") to LArray(stackTrace.mapTo(mutableListOf()) { LString(it.toString()) })
        )
    }

    fun mismatchedArgs(stackTrace: List<StackTrace>): LObject {
        return create(
            "mismatchedArguments",
            "Invocation failed due to mismatched arguments.",
            stackTrace
        )
    }

    fun notAFunction(type: String, stackTrace: List<StackTrace>): LObject {
        return create(
            "notAFunction",
            "Cannot invoke function for type '$type'.",
            stackTrace
        )
    }

    fun noElementExists(element: String, stackTrace: List<StackTrace>): LObject {
        return create(
            "noElementExists",
            "Element '$element' does not exist.",
            stackTrace
        )
    }

    fun unsupportedOperation(
        operation: String,
        leftType: String,
        rightType: String,
        stackTrace: List<StackTrace>
    ): LObject {
        return create(
            "unsupportedOperation",
            "Cannot apply operation '$operation' for types '$leftType' and '$rightType'.",
            stackTrace
        )
    }

    fun unsupportedOperation(operation: String, type: String, stackTrace: List<StackTrace>): LObject {
        return create(
            "unsupportedOperation",
            "Cannot apply operation '$operation' for type '$type'.",
            stackTrace
        )
    }

    fun toObject(e: LinNativeException, stackTrace: List<StackTrace>): LObject {
        return create(e.exceptionType, e.exceptionDescription, stackTrace)
    }

    fun fromNative(e: Exception, stackTrace: List<StackTrace>): LAny {
        return create("nativeException", e.toString(), stackTrace)
    }
}
