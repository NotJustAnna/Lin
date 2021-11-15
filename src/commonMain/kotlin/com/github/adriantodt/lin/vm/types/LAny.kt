package com.github.adriantodt.lin.vm.types

public sealed class LAny {
    public abstract fun truth(): Boolean

    public abstract val linType: String

    public abstract fun getMember(name: String): LAny?

    public companion object {
        public fun of(value: Any?): LAny {
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

        public fun ofBoolean(value: Boolean): LAny {
            return if (value) LTrue else LFalse
        }

        public fun ofEntry(entry: Map.Entry<LAny, LAny>): LAny {
            return LObject(LString("key") to entry.key, LString("value") to entry.value)
        }
    }
}
