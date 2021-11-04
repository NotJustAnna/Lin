package net.notjustanna.lin.vm.impl

import net.notjustanna.lin.vm.types.LAny
import net.notjustanna.lin.vm.types.LObject
import net.notjustanna.lin.vm.types.LString

object Exceptions {
    // TODO Add support for scope/lines

    private fun create(type: String, description: String): LAny {
        return LObject(
            LString("errorType") to LString(type),
            LString("description") to LString(description)
        )
    }

    fun mismatchedArgs(): LAny {
        return create(
            "mismatchedArguments",
            "Invocation failed due to mismatched arguments."
        )
    }

    fun notAFunction(type: String): LAny {
        return create(
            "notAFunction",
            "Cannot invoke function for type '$type'."
        )
    }

    fun unsupportedOperation(operation: String, leftType: String, rightType: String): LAny {
        return create(
            "unsupportedOperation",
            "Cannot apply operation '$operation' for types '$leftType' and '$rightType'."
        )
    }

    fun unsupportedOperation(operation: String, type: String): LAny {
        return create(
            "unsupportedOperation",
            "Cannot apply operation '$operation' for type '$type'."
        )
    }
}
