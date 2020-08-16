package io.github.cafeteriaguild.lin.rt.exceptions

import io.github.cafeteriaguild.lin.rt.lib.LObj
import io.github.cafeteriaguild.lin.rt.lib.nativelang.routes.LinCatchable

class LinThrownException(override val thrown: LObj) : LinException(thrown.toString()), LinCatchable
