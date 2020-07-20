package io.github.cafeteriaguild.lin.rt.lib

class LMutableList(override val list: MutableList<LObj>) : LList(list) {
    override fun set(args: List<LObj>, value: LObj) {
        list[(args.single() as LNumber).value.toInt()] = value
    }
}