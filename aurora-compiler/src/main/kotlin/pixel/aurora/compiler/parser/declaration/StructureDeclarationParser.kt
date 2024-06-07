package pixel.aurora.compiler.parser.declaration

import pixel.aurora.compiler.parser.*
import pixel.aurora.compiler.parser.expression.IdentifierParser
import pixel.aurora.compiler.parser.other.AnnotationUsingParser
import pixel.aurora.compiler.parser.other.TypeParameterParser
import pixel.aurora.compiler.parser.other.VisibilityModeParser
import pixel.aurora.compiler.parser.type.SimpleTypeParser
import pixel.aurora.compiler.parser.util.ListParser
import pixel.aurora.compiler.tree.FunctionDeclaration
import pixel.aurora.compiler.tree.StructureDeclaration
import pixel.aurora.compiler.tree.other.VisibilityMode

class StructureDeclarationParser : Parser<StructureDeclaration>() {

    override fun parse(): StructureDeclaration {
        val annotations = include(ListParser(AnnotationUsingParser(), "@[", "]", ",").optional()).getOrElse { emptyList() }
        val visibilityMode = include(VisibilityModeParser().optional()).getOrElse { VisibilityMode.PUBLIC }
        buffer.get().expectIdentifier("struct")
        val name = include(IdentifierParser())
        val typeParameters = include(ListParser(TypeParameterParser(), "<", ">").optional()).getOrElse { emptyList() }
        val extends = include(ListParser(SimpleTypeParser(), ":", null).optional()).getOrElse { emptyList() }
        val body = include(ListParser(TopLevelDeclarationParser(), "{", "}", null).optional()).getOrElse { emptyList() }
        for (declaration in body) {
            if (declaration is FunctionDeclaration) throw makeError("Unexpected function declaration")
        }
        return StructureDeclaration(name, annotations, extends, typeParameters, body, visibilityMode)
    }

}