package net.notjustanna.lin.vm.types

sealed class LAny {
    abstract fun truth(): Boolean

    companion object {
        fun of(value: Any?): LAny {
            return when (value) {
                null, is Unit -> LNull
                true -> LTrue
                false -> LFalse
                is String -> LString(value)
                is Char -> LString(value.toString())
                is Number -> if (value is Float || value is Double) {
                    LDecimal(value.toDouble())
                } else {
                    LInteger(value.toLong())
                }
                is List<*> -> LArray(value.mapTo(mutableListOf()) { of(it) })
                is Map<*, *> -> LObject(value.entries.associateTo(mutableMapOf()) { of(it.key) to of(it.value) })
                else -> throw IllegalArgumentException("Can't convert $value to LAny.")
            }
        }
    }
}
