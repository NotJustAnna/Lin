package net.notjustanna.lin.vm

import net.notjustanna.lin.vm.types.LAny

public class LAnyException(public val value: LAny) : RuntimeException("Object thrown from Lin: $value")
