package pixel.aurora.compiler.parser

import pixel.aurora.compiler.parser.declaration.*
import pixel.aurora.compiler.tree.Declaration

class CommonDeclarationParser : Parser<Declaration>() {

    override fun parse(): Declaration {
        return include(EmptyDeclarationParser() or FunctionDeclarationParser() or VariableDeclarationParser())
    }

}

class TopLevelDeclarationParser : Parser<Declaration>() {

    fun choices() =
        CommonDeclarationParser() or ClassDeclarationParser() or SingletonObjectDeclarationParser() or InterfaceDeclarationParser() or StructureDeclarationParser() or TypeDeclarationParser()

    override fun parse(): Declaration {
        return include(choices())
    }

}
