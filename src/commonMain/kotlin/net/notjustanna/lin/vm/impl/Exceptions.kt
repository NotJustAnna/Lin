package net.notjustanna.lin.vm.impl

import net.notjustanna.lin.exception.LinNativeException
import net.notjustanna.lin.vm.StackTrace
import net.notjustanna.lin.vm.types.LAny
import net.notjustanna.lin.vm.types.LArray
import net.notjustanna.lin.vm.types.LObject
import net.notjustanna.lin.vm.types.LString

public object Exceptions {
    public fun create(type: String, description: String, stackTrace: List<StackTrace>): LObject {
        return LObject(
            LString("errorType") to LString(type),
            LString("description") to LString(description),
            LString("stackTrace") to LArray(stackTrace.mapTo(mutableListOf()) { LString(it.toString()) })
        )
    }

    public fun mismatchedArgs(stackTrace: List<StackTrace>): LObject {
        return create(
            "mismatchedArguments",
            "Invocation failed due to mismatched arguments.",
            stackTrace
        )
    }

    public fun notAFunction(type: String, stackTrace: List<StackTrace>): LObject {
        return create(
            "notAFunction",
            "Cannot invoke function for type '$type'.",
            stackTrace
        )
    }

    public fun noElementExists(element: String, stackTrace: List<StackTrace>): LObject {
        return create(
            "noElementExists",
            "Element '$element' does not exist.",
            stackTrace
        )
    }

    public fun unsupportedOperation(
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

    public fun unsupportedOperation(operation: String, type: String, stackTrace: List<StackTrace>): LObject {
        return create(
            "unsupportedOperation",
            "Cannot apply operation '$operation' for type '$type'.",
            stackTrace
        )
    }

    public fun toObject(e: LinNativeException, stackTrace: List<StackTrace>): LObject {
        return create(e.exceptionType, e.exceptionDescription, stackTrace)
    }

    public fun fromNative(e: Exception, stackTrace: List<StackTrace>): LAny {
        return create("nativeException", e.toString(), stackTrace)
    }
}
