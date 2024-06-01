package pixel.aurora.parser

import pixel.aurora.parser.declaration.EmptyDeclarationParser
import pixel.aurora.tree.Declaration
import pixel.aurora.tree.EmptyDeclaration
import pixel.aurora.tree.Program

class ProgramParser : Parser<Program>() {

    override fun parse(): Program {
        val declarations = mutableListOf<Declaration>()
        while (buffer.hasNext()) {
            declarations += include(choose(EmptyDeclarationParser, DeclarationParser()))
        }
        declarations.removeIf { it is EmptyDeclaration }
        return Program(declarations)
    }

}