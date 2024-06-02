package pixel.aurora.compiler.parser.declaration

import pixel.aurora.compiler.parser.*
import pixel.aurora.compiler.parser.expression.IdentifierParser
import pixel.aurora.compiler.parser.other.TypeParameterParser
import pixel.aurora.compiler.parser.other.VisibilityModeParser
import pixel.aurora.compiler.parser.type.SimpleTypeParser
import pixel.aurora.compiler.parser.util.ListParser
import pixel.aurora.compiler.tree.InterfaceDeclaration
import pixel.aurora.compiler.tree.VariableDeclaration
import pixel.aurora.compiler.tree.other.VisibilityMode

class InterfaceDeclarationParser : Parser<InterfaceDeclaration>() {

    override fun parse(): InterfaceDeclaration {
        val visibilityMode = include(VisibilityModeParser().optional()).getOrElse { VisibilityMode.PUBLIC }
        buffer.get().expectIdentifier("interface")
        val name = include(IdentifierParser())
        val typeParameters = include(ListParser(TypeParameterParser(), "<", ">").optional()).getOrElse { emptyList() }
        val implements = include(implementsPart().optional()).getOrElse { emptyList() }
        val body = include(ListParser(TopLevelDeclarationParser(), "{", "}", null).optional()).getOrElse { emptyList() }
        for (declaration in body) {
            if (declaration is VariableDeclaration) throw makeError()
        }
        return InterfaceDeclaration(name, typeParameters, implements, body, visibilityMode)
    }

    fun implementsPart() = parser {
        include(ListParser(SimpleTypeParser(), ":", null))
    }

}