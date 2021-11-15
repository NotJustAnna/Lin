package net.notjustanna.lin.vm.types

public sealed class LNumber : LAny() {
    public abstract operator fun compareTo(other: LNumber): Int

    public abstract operator fun plus(right: LNumber): LNumber

    public abstract operator fun minus(right: LNumber): LNumber

    public abstract operator fun times(right: LNumber): LNumber

    public abstract operator fun div(right: LNumber): LNumber

    public abstract operator fun rem(right: LNumber): LNumber

    public abstract operator fun unaryPlus(): LNumber

    public abstract operator fun unaryMinus(): LNumber
}
