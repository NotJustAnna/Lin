package xyz.avarel.lobos.parser

import xyz.avarel.lobos.ast.expr.files.FileModuleExpr
import xyz.avarel.lobos.ast.expr.files.FolderModuleExpr
import xyz.avarel.lobos.lexer.Section
import xyz.avarel.lobos.lexer.Source
import xyz.avarel.lobos.lexer.Tokenizer
import java.io.File

class FolderParser(val grammar: Grammar, val folder: File) {
    init {
        require(folder.isDirectory) { "${folder.name} is not a folder." }
    }

    val errors: MutableList<SyntaxException> = mutableListOf()

    fun parse(): FolderModuleExpr {
        val folderModules: MutableList<FolderModuleExpr> = mutableListOf()
        val fileModules: MutableList<FileModuleExpr> = mutableListOf()

        for (child in folder.listFiles()) {
            if (child.isDirectory) {
                folderModules += FolderParser(grammar, child).parse()
            } else {
                if (child.name.endsWith(".waf")) {
                    fileModules += parseFile(child)
                }
            }
        }

        return FolderModuleExpr(
            folder.nameWithoutExtension,
            folderModules,
            fileModules,
            Section(Source(folder), -1, -1, -1)
        )
    }

    private fun parseFile(file: File): FileModuleExpr {
        val src = Source(file)
        val lexer = Tokenizer(src)

        val parser = Parser(grammar, src, lexer.parse())
        val declarations = parser.parseDeclarations(null)
        errors += parser.errors

        return FileModuleExpr(file.nameWithoutExtension, declarations, Section(Source(file), -1, -1, -1))
    }
}