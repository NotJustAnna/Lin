package io.github.cafeteriaguild.lin.rt.lib

interface LNumber : LObj {
    val value: Number

    operator fun plus(other: LNumber) = box(plus(value, other.value))

    operator fun minus(other: LNumber) = box(minus(value, other.value))

    operator fun times(other: LNumber) = box(times(value, other.value))

    operator fun div(other: LNumber) = box(div(value, other.value))

    operator fun rem(other: LNumber) = box(rem(value, other.value))

    operator fun unaryPlus() = box(unaryPlus(value))

    operator fun unaryMinus() = box(unaryMinus(value))

    operator fun compareTo(other: LNumber) = compareTo(value, other.value)

    companion object {
        fun box(target: Number): LNumber {
            return when (target) {
                is Double -> LDouble(target)
                is Float -> LFloat(target)
                is Long -> LLong(target)
                else -> LInt(target.toInt())
            }
        }

        fun plus(left: Number, right: Number): Number {
            return when {
                left is Double || right is Double -> left.toDouble() + right.toDouble()
                left is Float || right is Float -> left.toFloat() + right.toFloat()
                left is Long || right is Long -> left.toLong() + right.toLong()
                else -> left.toInt() + right.toInt()
            }
        }

        fun minus(left: Number, right: Number): Number {
            return when {
                left is Double || right is Double -> left.toDouble() - right.toDouble()
                left is Float || right is Float -> left.toFloat() - right.toFloat()
                left is Long || right is Long -> left.toLong() - right.toLong()
                else -> left.toInt() - right.toInt()
            }
        }

        fun times(left: Number, right: Number): Number {
            return when {
                left is Double || right is Double -> left.toDouble() * right.toDouble()
                left is Float || right is Float -> left.toFloat() * right.toFloat()
                left is Long || right is Long -> left.toLong() * right.toLong()
                else -> left.toInt() * right.toInt()
            }
        }

        fun div(left: Number, right: Number): Number {
            return when {
                left is Double || right is Double -> left.toDouble() / right.toDouble()
                left is Float || right is Float -> left.toFloat() / right.toFloat()
                left is Long || right is Long -> left.toLong() / right.toLong()
                else -> left.toInt() / right.toInt()
            }
        }

        fun rem(left: Number, right: Number): Number {
            return when {
                left is Double || right is Double -> left.toDouble() % right.toDouble()
                left is Float || right is Float -> left.toFloat() % right.toFloat()
                left is Long || right is Long -> left.toLong() % right.toLong()
                else -> left.toInt() / right.toInt()
            }
        }

        fun unaryMinus(target: Number): Number {
            return when (target) {
                is Double -> -target.toDouble()
                is Float -> -target.toFloat()
                is Long -> -target.toLong()
                else -> -target.toInt()
            }
        }

        fun unaryPlus(target: Number): Number {
            return when (target) {
                is Double -> +target.toDouble()
                is Float -> +target.toFloat()
                is Long -> +target.toLong()
                else -> +target.toInt()
            }
        }

        fun compareTo(left: Number, right: Number): Int {
            return when {
                left is Double || right is Double -> left.toDouble().compareTo(right.toDouble())
                left is Float || right is Float -> left.toFloat().compareTo(right.toFloat())
                left is Long || right is Long -> left.toLong().compareTo(right.toLong())
                else -> left.toInt() / right.toInt()
            }
        }

    }
}