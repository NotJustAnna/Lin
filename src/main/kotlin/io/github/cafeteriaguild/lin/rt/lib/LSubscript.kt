package io.github.cafeteriaguild.lin.rt.lib

interface LSubscript : LObj {
    operator fun get(args: List<LObj>): LObj
    operator fun set(args: List<LObj>, value: LObj)
}