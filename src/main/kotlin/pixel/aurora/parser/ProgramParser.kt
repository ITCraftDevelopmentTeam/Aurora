package pixel.aurora.parser

import pixel.aurora.tree.Declaration
import pixel.aurora.tree.Program


class DeclarationParser : Parser<Declaration>() {

    override fun parse(): Declaration {
        return include(choice(CommentsDeclarationParser(), FunctionDeclarationParser()))
    }

}

class ProgramParser : Parser<Program>() {

    override fun parse(): Program {
        val packageDefinition = include(PackageDefinitionStatementParser())
        include(whitespace())
        val body = repeat(
            parser {
                include(whitespace())
                val declaration = include(DeclarationParser())
                include(semicolon(true))
                declaration
            }
        ).let { include(it) }
        return Program(packageDefinition, body)
    }

}