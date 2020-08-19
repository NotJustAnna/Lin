package io.github.cafeteriaguild.lin.rt.exceptions.internal

import io.github.cafeteriaguild.lin.rt.exceptions.LinException
import io.github.cafeteriaguild.lin.rt.lib.LObj

class ReturnException(val value: LObj) : LinException("Generated return signal")
