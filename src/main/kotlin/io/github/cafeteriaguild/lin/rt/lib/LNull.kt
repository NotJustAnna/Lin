package io.github.cafeteriaguild.lin.rt.lib

import io.github.cafeteriaguild.lin.rt.exc.LinNullException
import io.github.cafeteriaguild.lin.rt.scope.Property

object LNull : LObj {
    override fun property(name: String): Property? {
        throw LinNullException()
    }

    override fun toString() = "null"
}