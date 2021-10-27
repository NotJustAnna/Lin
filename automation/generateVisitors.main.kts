import java.io.File
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors

object GenerateVisitorTask {
    private val rootSrc = File("src/commonMain/kotlin")
    private val astDir = File(rootSrc, "com/github/adriantodt/lin/ast")
    private val visitorDir = File(astDir, "visitor")
    private val nodeDir = File(astDir, "node")

    private val visitors = listOf(
        VisitorConfig(0, null),
        VisitorConfig(0, ReturnValue.INTERFACE),
        VisitorConfig(0, ReturnValue.PARAMETERIZED),
        VisitorConfig(1, null),
    )

    private val implementationsFound = mutableListOf<CollectedExprImpl>()
    private val interfacesFound = mutableListOf<CollectedExprInterface>()

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

    data class VisitorConfig(val paramCount: Int, val returnValue: ReturnValue?) {
        fun className() = "Node${returnValue?.classNamePrefix ?: ""}Visitor${if (paramCount != 0) paramCount else ""}${returnValue?.classNameSuffix ?: ""}"
        fun parameterizedClassName() = if (hasParamsOrTypedReturnValue())
            "${className()}<${typeParamsWithReturnValue().joinToString(", ")}>"
        else className()

        fun fqcn() = "${packageName()}.${className()}"
        fun typeParams() = typeParams.take(paramCount)
        fun typeParamsWithReturnValue() = if (returnValue == ReturnValue.PARAMETERIZED) typeParams() + 'R' else typeParams()
        fun hasParamsOrTypedReturnValue() = paramCount > 0 || returnValue == ReturnValue.PARAMETERIZED
        fun fileName() = "${className()}.kt"

        companion object {
            private val typeParams = ('T'..'Z') + ('A'..'Q') + 'S'

            fun packageName() = visitorDir.toRelativeString(rootSrc).replace('/', '.')
        }
    }

    enum class ReturnValue(val classNamePrefix: String, val classNameSuffix: String) {
        SELF("Self", ""),
        INTERFACE("Map", ""),
        PARAMETERIZED("","R");
    }

    data class CollectedExprImpl(val name: String, val fqcn: String, val superInterface: String)
    data class CollectedExprInterface(val name: String, val fqcn: String, val superInterface: String?)

    fun StringBuilder.appendIndent(indent: Int) = apply {
        for (ignored in 0 until indent) append(' ')
    }

    fun StringBuilder.tryImportSuperInterface(name: String) {
        val (_, fqcn, _) = interfacesFound.find { it.name == name } ?: return
        appendLine("import $fqcn")
    }

    private val automationRegex = Regex(
        """\/\*\s*\@automation\s*\(([\d\w\s.,]+)\)-start\s*\*\/([\s\S]*?)\/\*\s*\@automation-end\s*\*\/"""
    )

    private fun StringBuilder.generateExprInterfaceMethods(indentSize: Int, name: String) {
        for ((i, v) in visitors.withIndex()) {
            appendLine("import ${v.fqcn()}")
            appendIndent(indentSize)
            append("fun ")
            if (v.hasParamsOrTypedReturnValue()) {
                append('<')
                append(v.typeParamsWithReturnValue().joinToString(", "))
                append("> ")
            }
            append("accept(")
            append(
                (listOf("visitor: ${v.parameterizedClassName()}") + v.typeParams()
                    .mapIndexed { index, value -> "param$index: $value" }).joinToString(", ")
            )
            appendLine(when (v.returnValue) {
                ReturnValue.SELF, ReturnValue.INTERFACE -> "): $name"
                ReturnValue.PARAMETERIZED -> "): R"
                null -> ")"
            })

            if (visitors.lastIndex != i) {
                appendLine()
            }
        }
    }

    private fun StringBuilder.generateExprOverrideMethods(indentSize: Int, name: String) {
        val overrides = visitors.filter { it.returnValue == ReturnValue.INTERFACE || it.returnValue == ReturnValue.SELF }
        for ((i, v) in overrides.withIndex()) {
            appendLine("import ${v.fqcn()}")
            appendIndent(indentSize)
            append("override fun ")
            if (v.hasParamsOrTypedReturnValue()) {
                append('<')
                append(v.typeParamsWithReturnValue().joinToString(", "))
                append("> ")
            }
            append("accept(")
            append(
                (listOf("visitor: ${v.parameterizedClassName()}") + v.typeParams()
                    .mapIndexed { index, value -> "param$index: $value" }).joinToString(", ")
            )
            appendLine(when (v.returnValue) {
                ReturnValue.SELF, ReturnValue.INTERFACE -> "): $name"
                else -> error("Could not happen.")
            })

            if (overrides.lastIndex != i) {
                appendLine()
            }
        }
    }

    private fun StringBuilder.generateExprImplMethods(indentSize: Int, name: String, superInterface: String) {
        for ((i, v) in visitors.withIndex()) {
            appendLine("import ${v.fqcn()}")
            if (v.returnValue == ReturnValue.INTERFACE) {
                tryImportSuperInterface(superInterface)
            }
            appendIndent(indentSize)
            append("override fun ")
            if (v.hasParamsOrTypedReturnValue()) {
                append('<')
                append(v.typeParamsWithReturnValue().joinToString(", "))
                append("> ")
            }
            append("accept(")
            append(
                (listOf("visitor: ${v.parameterizedClassName()}") + v.typeParams()
                    .mapIndexed { index, value -> "param$index: $value" }).joinToString(", ")
            )
            append(when (v.returnValue) {
                ReturnValue.SELF -> "): $name"
                ReturnValue.INTERFACE -> "): $superInterface"
                ReturnValue.PARAMETERIZED -> "): R"
                null -> ")"
            })
            append(" = visitor.visit$name(")
            append((listOf("this") + List(v.typeParams().size) { "param$it" }).joinToString(", "))
            appendLine(')')

            if (visitors.lastIndex != i) {
                appendLine()
            }
        }
    }

    private fun generateVisitor(v: VisitorConfig) {
        val text = buildString {
            var indent = 0
            appendLine("package ${VisitorConfig.packageName()}")
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
            appendLine(
                when(v.returnValue) {
                    ReturnValue.SELF -> " and with it's own type as return value."
                    ReturnValue.INTERFACE -> " and with its interface as return value."
                    ReturnValue.PARAMETERIZED -> " and with a parameterized return value."
                    null -> " and no return value."
                }
            )
            appendLine(" * NOTE: This file is generated!")
            appendLine(" */")
            append("interface ${v.parameterizedClassName()}")
            appendLine(" {")
            indent++
            val impls = implementationsFound.sortedBy { it.name }
            for ((i, impl) in impls.withIndex()) {
                appendLine("import ${impl.fqcn}")
                if (v.returnValue == ReturnValue.INTERFACE) {
                    tryImportSuperInterface(impl.superInterface)
                }

                appendIndent(indent * 4)
                append("fun visit${impl.name}(")
                append(
                    (listOf("node: ${impl.name}") + v.typeParams()
                        .mapIndexed { index, value -> "param$index: $value" }).joinToString(", ")
                )
                appendLine(when (v.returnValue) {
                    ReturnValue.SELF -> "): ${impl.name}"
                    ReturnValue.INTERFACE -> "): ${impl.superInterface}"
                    ReturnValue.PARAMETERIZED -> "): R"
                    null -> ")"
                })

                if (impls.lastIndex != i) {
                    appendLine()
                }
            }
            appendLine('}')
        }

        File(visitorDir, v.fileName()).writeText(reOrderImportsOnDemand(text))
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

                val pkg = text.lines()
                    .firstOrNull { it.trimStart().startsWith("package") }
                    ?.trim()?.removePrefix("package ")?.plus('.') ?: ""

                when {
                    automationStr.startsWith("ast.root ") -> {
                        val name = automationStr.removePrefix("ast.root ")

                        synchronized(interfacesFound) {
                            interfacesFound.add(CollectedExprInterface(name, pkg+name, null))
                        }

                        generateExprInterfaceMethods(minCommonIndent, name)
                    }
                    automationStr.startsWith("ast.override ") -> {
                        val (name, superInterface) = automationStr.removePrefix("ast.override ").split(",", limit = 2).map(String::trim)

                        synchronized(interfacesFound) {
                            interfacesFound.add(CollectedExprInterface(name, pkg+name, superInterface))
                        }

                        generateExprOverrideMethods(minCommonIndent, name)
                    }
                    automationStr.startsWith("ast.impl ") -> {
                        val (name, superInterface) = automationStr.removePrefix("ast.impl ").split(",", limit = 2).map(String::trim)

                        synchronized(implementationsFound) {
                            implementationsFound.add(CollectedExprImpl(name, pkg+name, superInterface))
                        }

                        generateExprImplMethods(minCommonIndent, name, superInterface)
                    }
                }

                appendIndent(minCommonIndent).append("/* @automation-end */")

                currentIndex = end + 1
            }
            if (text.lastIndex > currentIndex) {
                append(text.substring(currentIndex, text.lastIndex))
            }
        }

        file.writeText(reOrderImportsOnDemand(newText))
    }

    private fun reOrderImportsOnDemand(text: String): String {
        return buildString {
            val (first, code) = text.lines().partition {
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
                // TODO Ignore imports if same-package
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
    }
}

GenerateVisitorTask.run()
