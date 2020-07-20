package io.github.cafeteriaguild.lin.rt.lib

interface LCallable : LObj {
    operator fun invoke(args: List<LObj>): LObj
}