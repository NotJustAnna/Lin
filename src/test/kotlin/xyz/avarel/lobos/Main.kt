package xyz.avarel.lobos

import xyz.avarel.lobos.ast.ASTViewer
import xyz.avarel.lobos.lexer.Source
import xyz.avarel.lobos.lexer.Tokenizer
import xyz.avarel.lobos.parser.*
import xyz.avarel.lobos.tc.TypeChecker
import xyz.avarel.lobos.tc.scope.DefaultScopeContext
import xyz.avarel.lobos.tc.scope.StmtContext
import java.io.File
import java.util.*

/* Smart Compiler
let y: 1|3|5|7|"string" = "string";
let x: String = y
 */ // The compiler should remember that y is effectively "string"
// so the assignment to x should be legal

// OR -> add inverse assumption to scope
// AND -> nothing

/*
        let y = "hello";
        let x: str = y + 2;
        y = "world";
        return 3;
        let z: "world" = y;
 */

/*
        let a: i32 | null = 3;
        if a == null {
            let b: null = a;
            return ();
        } else {
            let b: i32 = a;
        };
        let b: 3 = a;
 */

/*
let a: i32 | null = 1;

        if (a == null || a == 1 || a == 2) {
            let b: () = a;
            return ();
        };

        let b: () = a;
 */

/*
    let b: () = a;
                └── Expected () but found [null | 2 | 1] at (_:4:16)
let b: () = a;
            └── Expected () but found [i32 ! [1 | 2]] at (_:8:12)
 */

// dank::meme
// dank.meme    // if arity-0 -> invoke
// else if is function -> require next is R_PAREN (
// else -> property access

/*
struct Point {
    x: i32
    y: i32
}
 */

fun main() {
    mainLocalFolder()
}

fun mainLocalFolder() {
    val parser = FolderParser(DefaultGrammar, File("./scripts"))
    val ast = parser.parse()

    println()
    println("|> ERRORS:")

    ast.accept(TypeChecker(
        DefaultScopeContext.subContext(),
        StmtContext(),
        false
    ) { message, section ->
        parser.errors += TypeException(message, section)
    })

    printErrors(parser.errors)

    println()
    println("|> AST")
    println(buildString { ast.accept(ASTViewer(this, "", true)) })
}

fun mainLocalFile() {
    val source = Source(File("scripts/script.waf"))

    val lexer = Tokenizer(source)
    val tokens = lexer.parse()

    val parser = Parser(DefaultGrammar, source, tokens)
    val ast = parser.parse()

    ast.accept(TypeChecker(
        DefaultScopeContext.subContext(),
        StmtContext(),
        false
    ) { message, section ->
        parser.errors += TypeException(message, section)
    })


    println()
    println("|> ERRORS:")

    printErrors(parser.errors)

    println()
    println("|> AST")
    println(buildString { ast.accept(ASTViewer(this, "", true)) })
}

fun mainConsole() {
    val sc = Scanner(System.`in`)
    val ctx = DefaultScopeContext.subContext()
    while (true) {
        print(">>> ")
        val line = sc.nextLine()
        if (line.isNullOrEmpty()) break

        val source = Source(line)

        val lexer = Tokenizer(source)
        val tokens = lexer.parse()

        val parser = Parser(DefaultGrammar, source, tokens)
        val ast = parser.parse()

        ast.accept(TypeChecker(
            ctx,
            StmtContext(),
            false
        ) { message, section ->
            parser.errors += TypeException(message, section)
        })


        println()
        println("|> ERRORS:")

        printErrors(parser.errors)

        println()
    }
}

fun printErrors(list: List<SyntaxException>) {
    if (list.isEmpty()) {
        println("No errors.")
        return
    }

    list.forEach {
//        it.printStackTrace(System.out)
        val line = it.position.getLine()
        val msg = buildString {
            append(line)
            append('\n')
            kotlin.repeat(it.position.lineIndex) {
                append(' ')
            }
            when (it.position.length) {
                0, 1 -> append("^ ")
                else -> {
                    append('└')
                    kotlin.repeat(it.position.length - 2) {
                        append('─')
                    }
                    append("┘ ")
                }
            }
            append(it.message)
        }
        println(msg)
    }
}