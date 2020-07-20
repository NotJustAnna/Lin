package io.github.cafeteriaguild.lin.rt.lib

interface LCallable : LObj {
    operator fun invoke(parameters: List<LObj>): LObj
}