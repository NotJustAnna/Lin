import java.io.File
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors

object GenerateVisitorTask {
    val rootSrc = File("src/commonMain/kotlin")
    val astDir = File(rootSrc, "com/github/adriantodt/lin/ast")
    val visitorDir = File(astDir, "visitor")
    val nodeDir = File(astDir, "node")

    val visitors = listOf(
        VisitorConfig(0, false),
        VisitorConfig(0, true),
        VisitorConfig(1, false),
    )

    val implementationsFound = mutableListOf<Pair<String, String>>()

    fun run() {
        val start = System.currentTimeMillis()

        check(rootSrc.isDirectory) { "rootSrc Folder does not exist!" }
        check(astDir.isDirectory) { "AST Folder does not exist!" }
        check(astDir.isDirectory) { "AST/Node Folder does not exist!" }
        visitorDir.deleteRecursively()
        visitorDir.mkdirs()

        val executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2)

        val fileAutomationTasks = nodeDir.walk().filter { it.isFile && it.extension == "kt" }.map {
            CompletableFuture.runAsync({ processFile(it) }, executor)
        }.toList()

        fileAutomationTasks.forEach { it.join() }

        val fileCreationTasks = visitors.map {
            CompletableFuture.runAsync({ generateVisitor(it) }, executor)
        }

        fileCreationTasks.forEach { it.join() }

        executor.shutdown()
        println("Finished in ${System.currentTimeMillis() - start}ms.")
    }

    data class VisitorConfig(val paramCount: Int, val returnValue: Boolean) {
        fun className() = "NodeVisitor${paramCount}${if (returnValue) "R" else ""}"
        fun parameterizedClassName() = if (hasParamsOrReturnValue())
            "${className()}<${typeParamsWithReturnValue().joinToString(", ")}>"
        else className()

        fun fqcn() = "${packageName()}.${className()}"
        fun typeParams() = typeParams.take(paramCount)
        fun typeParamsWithReturnValue() = if (!returnValue) typeParams() else typeParams() + 'R'
        fun hasParamsOrReturnValue() = paramCount > 0 || returnValue
        fun fileName() = "${className()}.kt"

        companion object {
            private val typeParams = ('T'..'Z') + ('A'..'Q') + 'S'

            fun packageName() = visitorDir.toRelativeString(rootSrc).replace('/', '.')
        }
    }

    fun StringBuilder.appendIndent(indent: Int) = apply {
        for (ignored in 0 until indent) append(' ')
    }

    private val automationRegex = Regex(
        """\/\*\s*\@automation\s*\(([\d\w\s.]+)\)-start\s*\*\/([\s\S]*?)\/\*\s*\@automation-end\s*\*\/"""
    )

    private fun StringBuilder.generateExprInterfaceMethods(indentSize: Int) {
        for ((i, v) in visitors.withIndex()) {
            appendLine("import ${v.fqcn()}")
            appendIndent(indentSize)
            append("fun ")
            if (v.hasParamsOrReturnValue()) {
                append('<')
                append(v.typeParamsWithReturnValue().joinToString(", "))
                append("> ")
            }
            append("accept(")
            append(
                (listOf("visitor: ${v.parameterizedClassName()}") + v.typeParams()
                    .mapIndexed { index, value -> "param$index: $value" }).joinToString(", ")
            )
            appendLine(if (v.returnValue) "): R" else ")")

            if (visitors.lastIndex != i) {
                appendLine()
            }
        }
    }

    fun StringBuilder.generateExprImplMethods(indentSize: Int, name: String) {
        for ((i, v) in visitors.withIndex()) {
            appendLine("import ${v.fqcn()}")
            appendIndent(indentSize)
            append("override fun ")
            if (v.hasParamsOrReturnValue()) {
                append('<')
                append(v.typeParamsWithReturnValue().joinToString(", "))
                append("> ")
            }
            append("accept(")
            append(
                (listOf("visitor: ${v.parameterizedClassName()}") + v.typeParams()
                    .mapIndexed { index, value -> "param$index: $value" }).joinToString(", ")
            )
            append(if (v.returnValue) "): R" else ")")
            append(" = visitor.visit$name(")
            append((listOf("this") + List(v.typeParams().size) { "param$it" }).joinToString(", "))
            appendLine(')')

            if (visitors.lastIndex != i) {
                appendLine()
            }
        }
    }

    fun generateVisitor(v: VisitorConfig) {
        val text = buildString {
            var indent = 0
            appendLine("package ${VisitorConfig.packageName()}")
            appendLine()

            val (names, fqcns) = implementationsFound.unzip()
            for (fqcn in fqcns) {
                appendLine("import $fqcn")
            }
            appendLine()
            appendLine("/**")
            append(" * A Node Visitor with ")
            if (v.paramCount == 0) {
                append("no parameters")
            } else if (v.paramCount == 1) {
                append("1 parameter")
            } else {
                append("${v.paramCount} parameters")
            }
            appendLine(if (v.returnValue) " and with a return value." else " and no return value.")
            appendLine(" * NOTE: This file is generated!")
            appendLine(" */")
            append("interface ${v.parameterizedClassName()}")
            appendLine(" {")
            indent++
            for ((i, name) in names.withIndex()) {
                appendIndent(indent * 4)
                append("fun visit$name(")
                append(
                    (listOf("node: $name") + v.typeParams()
                        .mapIndexed { index, value -> "param$index: $value" }).joinToString(", ")
                )
                appendLine(if (v.returnValue) "): R" else ")")

                if (names.lastIndex != i) {
                    appendLine()
                }
            }
            appendLine('}')
        }

        File(visitorDir, v.fileName()).writeText(text)
    }

    fun processFile(file: File) {
        val text = file.readText()
        if (text.isEmpty()) return
        val matches = automationRegex.findAll(text).toList()
        if (matches.isEmpty()) return
        println("Found: $file -- ${matches.map { it.groupValues[1] }}")

        val newText = buildString {
            var currentIndex = 0
            for (match in matches) {
                val start = match.range.first
                val end = match.range.last

                if (start > currentIndex) {
                    append(text.substring(currentIndex, start))
                }

                val automationStr = match.groupValues[1]
                val minCommonIndent = match.groupValues[2].lines().let { lines ->
                    lines.filterIndexed { index, s -> index == lines.lastIndex || s.isNotBlank() }
                        .map { s -> s.indexOfFirst { !it.isWhitespace() }.let { if (it == -1) s.length else it } }
                        .minOrNull() ?: 0
                }
                appendLine("/* @automation($automationStr)-start */")

                when {
                    automationStr == "ast.node" -> {
                        generateExprInterfaceMethods(minCommonIndent)
                    }
                    automationStr.startsWith("ast.impl") -> {
                        val name = automationStr.removePrefix("ast.impl ")
                        val pkg = text.lines()
                            .firstOrNull { it.trimStart().startsWith("package") }
                            ?.trim()?.removePrefix("package ")?.plus('.') ?: ""

                        synchronized(implementationsFound) {
                            implementationsFound.add(name to (pkg+name))
                        }

                        generateExprImplMethods(minCommonIndent, name)
                    }
                }

                appendIndent(minCommonIndent).append("/* @automation-end */")

                currentIndex = end + 1
            }
            if (text.lastIndex > currentIndex) {
                append(text.substring(currentIndex, text.lastIndex))
            }
        }

        val fixedNewText = buildString {
            val (first, code) = newText.lines().partition {
                it.trimStart().let { s -> s.startsWith("import") || s.startsWith("package") }
            }

            val dejavuSet = mutableSetOf<String>()
            val wildcardSet = mutableSetOf<String>()


            val (packageDecl, imports) = first.partition { it.trimStart().startsWith("package") }

            packageDecl.forEach {
                appendLine(it)
                appendLine()
            }

            for (it in imports) {
                val s = it.trim()
                if (s !in dejavuSet && s.replaceAfterLast('.', ".*") !in wildcardSet) {
                    if (s.startsWith("import ${VisitorConfig.packageName()}")) {
                        val visitorImport = s.removePrefix("import ")
                        if (visitors.none { visitorImport == it.fqcn() }) continue
                    }
                    appendLine(it)
                    if (s.endsWith(".*")) {
                        wildcardSet.add(s)
                    } else {
                        dejavuSet.add(s)
                    }
                }
            }

            appendLine()

            code.dropWhile { it.isBlank() }.forEach { appendLine(it) }
        }

        file.writeText(fixedNewText)
    }
}

GenerateVisitorTask.run()
