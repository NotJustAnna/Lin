package net.notjustanna.lin.vm.types

sealed class LNumber : LAny() {
    abstract operator fun compareTo(other: LNumber): Int

    abstract operator fun plus(right: LNumber): LNumber

    abstract operator fun minus(right: LNumber): LNumber

    abstract operator fun times(right: LNumber): LNumber

    abstract operator fun div(right: LNumber): LNumber

    abstract operator fun rem(right: LNumber): LNumber

    abstract operator fun unaryPlus(): LNumber

    abstract operator fun unaryMinus(): LNumber
}
