package pixel.aurora.compiler.parser

import pixel.aurora.compiler.parser.declaration.EmptyDeclarationParser
import pixel.aurora.compiler.parser.declaration.PackageDeclarationParser
import pixel.aurora.compiler.parser.declaration.TopLevelAnnotationUsingDeclarationParser
import pixel.aurora.compiler.tree.Declaration
import pixel.aurora.compiler.tree.EmptyDeclaration
import pixel.aurora.compiler.tree.Program

class ProgramParser : Parser<Program>() {

    override fun parse(): Program {
        val annotations = include(TopLevelAnnotationUsingDeclarationParser().optional()).getOrElse { emptyList() }
        val packageDeclaration = include(PackageDeclarationParser())
        val declarations = mutableListOf<Declaration>()
        while (buffer.hasNext()) {
            declarations += include(choose(EmptyDeclarationParser, TopLevelDeclarationParser()))
        }
        declarations.removeIf { it is EmptyDeclaration }
        return Program(packageDeclaration, declarations, annotations)
    }

}