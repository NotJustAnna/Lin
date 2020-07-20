package io.github.cafeteriaguild.lin.oldrt.types

/**
 * Represents a Lin function's parameter
 */
interface LParameter {
    val type: LType
    val isReceiver: Boolean
    val isOptional: Boolean
}