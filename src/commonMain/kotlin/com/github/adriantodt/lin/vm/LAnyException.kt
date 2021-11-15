package com.github.adriantodt.lin.vm

import com.github.adriantodt.lin.vm.types.LAny

public class LAnyException(public val value: LAny) : RuntimeException("Object thrown from Lin: $value")
