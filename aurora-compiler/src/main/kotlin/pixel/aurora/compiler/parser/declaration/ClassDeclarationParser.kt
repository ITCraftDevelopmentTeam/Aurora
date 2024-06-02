package pixel.aurora.compiler.parser.declaration

import pixel.aurora.compiler.parser.*
import pixel.aurora.compiler.parser.expression.IdentifierParser
import pixel.aurora.compiler.parser.other.AnnotationUsingParser
import pixel.aurora.compiler.parser.other.ExtendPartParser
import pixel.aurora.compiler.parser.other.TypeParameterParser
import pixel.aurora.compiler.parser.other.VisibilityModeParser
import pixel.aurora.compiler.parser.util.ListParser
import pixel.aurora.compiler.tokenizer.TokenType
import pixel.aurora.compiler.tree.ClassDeclaration
import pixel.aurora.compiler.tree.SimpleType
import pixel.aurora.compiler.tree.other.ClassCall
import pixel.aurora.compiler.tree.other.VisibilityMode

class ClassDeclarationParser : Parser<ClassDeclaration>() {

    override fun parse(): ClassDeclaration {
        val annotations = include(AnnotationUsingParser.AnnotationUsingListParser.optional()).getOrElse { emptyList() }
        val visibilityMode = include(VisibilityModeParser().optional()).getOrElse { VisibilityMode.PUBLIC }
        val mode = include(
            parser {
                val identifier = buffer.get().expect(TokenType.IDENTIFIER).getRaw()
                ClassDeclaration.Mode.entries.first {
                    it.name.lowercase() == identifier
                }
            }.optional()
        ).getOrElse { ClassDeclaration.Mode.OPEN }
        buffer.get().expectIdentifier("class")
        val name = include(IdentifierParser())
        val typeParameters = include(ListParser(TypeParameterParser(), "<", ">").optional()).getOrElse { emptyList() }
        val extends = include(ExtendPartParser().optional()).getOrNull()
        val body = include(ListParser(TopLevelDeclarationParser(), "{", "}", null).optional()).getOrElse { emptyList() }
        return ClassDeclaration(
            name,
            extends?.second ?: emptyList(),
            extends?.first ?: ClassCall(SimpleType.Any, emptyList()),
            body,
            annotations,
            visibilityMode,
            typeParameters,
            mode
        )
    }

}
