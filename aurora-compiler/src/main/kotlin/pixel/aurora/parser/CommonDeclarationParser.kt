package pixel.aurora.parser

import pixel.aurora.parser.declaration.FunctionDeclarationParser
import pixel.aurora.parser.declaration.VariableDeclarationParser
import pixel.aurora.tree.Declaration

class CommonDeclarationParser : Parser<Declaration>() {

    override fun parse(): Declaration {
        return include(FunctionDeclarationParser() or VariableDeclarationParser())
    }

}

class TopLevelDeclarationParser : Parser<Declaration>() {

    override fun parse(): Declaration {
        return include(CommonDeclarationParser())
    }

}
