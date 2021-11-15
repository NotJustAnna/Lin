package net.notjustanna.lin.exception

public class LinNullPointerException : NullPointerException(), LinNativeException {
    override val exceptionType: String
        get() = "nullPointer"

    override val exceptionDescription: String
        get() = "Argument passed is null."
}
