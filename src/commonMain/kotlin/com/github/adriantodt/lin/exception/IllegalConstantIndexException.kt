package com.github.adriantodt.lin.exception

class IllegalConstantIndexException(index: Int) : IllegalArgumentException("Index #$index is not a valid constant")
