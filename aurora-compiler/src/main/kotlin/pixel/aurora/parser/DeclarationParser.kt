package pixel.aurora.parser

import pixel.aurora.parser.declaration.FunctionDeclarationParser
import pixel.aurora.tree.Declaration

class DeclarationParser : Parser<Declaration>() {

    override fun parse(): Declaration {
        return include(choose(FunctionDeclarationParser()))
    }

}