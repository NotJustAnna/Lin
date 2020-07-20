package io.github.cafeteriaguild.lin.rt.lib

import io.github.cafeteriaguild.lin.rt.exc.LinException
import io.github.cafeteriaguild.lin.rt.lib.dsl.createGetter
import io.github.cafeteriaguild.lin.rt.scope.Property

open class LList(open val list: List<LObj>) : LSubscript {
    private val size = createGetter { LInt(list.size) }
    override fun get(args: List<LObj>): LObj {
        return list.getOrNull((args.single() as LNumber).value.toInt()) ?: LNull
    }

    override fun set(args: List<LObj>, value: LObj) {
        throw LinException("Collection is read-only")
    }

    override fun property(name: String): Property? {
        return when (name) {
            "size" -> size
            else -> null
        }
    }

    override fun toString(): String {
        return list.toString()
    }
}

