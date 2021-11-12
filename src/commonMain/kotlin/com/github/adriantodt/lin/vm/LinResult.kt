package com.github.adriantodt.lin.vm

import com.github.adriantodt.lin.vm.types.LAny

sealed class LinResult {
    abstract fun getOrThrow(): LAny
    abstract fun getOrNull(): LAny?
    abstract fun thrownOrNull(): LAny?

    class Returned(val value: LAny) : LinResult() {
        override fun getOrThrow(): LAny {
            return value
        }

        override fun getOrNull(): LAny {
            return value
        }

        override fun thrownOrNull(): LAny? {
            return null
        }
    }

    class Thrown(val value: LAny) : LinResult() {
        override fun getOrThrow(): LAny {
            throw LAnyException(value)
        }

        override fun getOrNull(): LAny? {
            return null
        }

        override fun thrownOrNull(): LAny? {
            return value
        }
    }
}
