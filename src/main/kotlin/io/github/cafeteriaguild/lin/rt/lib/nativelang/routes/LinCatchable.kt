package io.github.cafeteriaguild.lin.rt.lib.nativelang.routes

import io.github.cafeteriaguild.lin.rt.lib.LObj

/**
 * [Throwable]s implementing this interface will be caught by Lin's try-catches
 */
interface LinCatchable {
    val thrown: LObj
}