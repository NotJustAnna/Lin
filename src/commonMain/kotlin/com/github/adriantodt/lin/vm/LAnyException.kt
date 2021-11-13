package com.github.adriantodt.lin.vm

import com.github.adriantodt.lin.vm.types.LAny

class LAnyException(val value: LAny) : RuntimeException("Object thrown from Lin: $value")
