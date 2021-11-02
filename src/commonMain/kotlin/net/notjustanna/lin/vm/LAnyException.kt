package net.notjustanna.lin.vm

import net.notjustanna.lin.vm.types.LAny

class LAnyException(val value: LAny) : RuntimeException("Object thrown from Lin")
