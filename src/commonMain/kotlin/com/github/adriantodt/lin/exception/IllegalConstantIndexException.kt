package com.github.adriantodt.lin.exception

public class IllegalConstantIndexException(index: Int) : IllegalArgumentException(
    "Index #$index is not a valid constant"
)
