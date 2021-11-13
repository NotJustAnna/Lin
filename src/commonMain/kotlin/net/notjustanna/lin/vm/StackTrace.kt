package net.notjustanna.lin.vm

data class StackTrace(
    val functionName: String,
    val sourceName: String? = null,
    val line: Int = -1,
    val column: Int = -1
) {
    override fun toString(): String {
        if (sourceName == null && line == -1 && column == -1) {
            return "$functionName[Platform]"
        }
        return "$functionName($sourceName:$line:$column)"
    }
}
