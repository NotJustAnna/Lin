package com.github.adriantodt.lin.exception

class IllegalConstantIndexException(index: Int) : RuntimeException("Index #$index is not a valid constant")
