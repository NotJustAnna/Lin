package io.github.cafeteriaguild.lin.utils

import net.notjustanna.tartar.api.lexer.Source
import net.notjustanna.tartar.api.parser.Token
import io.github.cafeteriaguild.lin.lexer.TokenType
import io.github.cafeteriaguild.lin.lexer.linStdLexer
import java.io.File
import java.util.concurrent.ForkJoinPool
import java.util.concurrent.atomic.AtomicLong
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

fun main() {
    val printlnLock = ReentrantLock()
    val count = AtomicLong()
    val unterminatedStringException = AtomicLong()
    val commonPool = ForkJoinPool.commonPool()
    File(".").walkTopDown().filter { it.isFile && it.extension == "kt" }.map {
        commonPool.submit {
            val tokens = ArrayList<Token<TokenType>>()
            try {
                linStdLexer.parseTo(Source(it), tokens)
                count.addAndGet(tokens.count { it.type != TokenType.INVALID }.toLong())
                val invalidTokens = tokens.filter { it.type == TokenType.INVALID }

                if (invalidTokens.isNotEmpty()) {
                    printlnLock.withLock {
                        println("Detected invalid tokens on $it:\n    ${invalidTokens.joinToString()}")
                    }
                }
            } catch (e: Exception) {
                //if (e is IllegalStateException && e.message == "Unterminated string") {
                //    unterminatedStringException.incrementAndGet()
                //} else {
                printlnLock.withLock {
                    println("File $it errored on lexing.")
                    println("Last 5 tokens were: ${tokens.takeLast(5)}")
                    e.printStackTrace(System.out)
                }
                ///}
            }
        }
    }.forEach { it.get() }
    println("Finished, valid tokens lexed: $count")
    if (unterminatedStringException.get() != 0L) {
        println("For debugging purposes, Unterminated string count is $unterminatedStringException")
    }
}