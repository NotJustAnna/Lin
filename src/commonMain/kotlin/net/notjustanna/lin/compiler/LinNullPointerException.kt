package net.notjustanna.lin.compiler

import net.notjustanna.lin.exception.LinNativeException

class LinNullPointerException : NullPointerException(), LinNativeException {
    override val exceptionType: String
        get() = "nullPointer"

    override val exceptionDescription: String
        get() = "Argument passed is null."
}
