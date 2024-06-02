package pixel.aurora.compiler.parser.declaration

import pixel.aurora.compiler.parser.*
import pixel.aurora.compiler.parser.expression.IdentifierParser
import pixel.aurora.compiler.parser.other.AnnotationUsingParser
import pixel.aurora.compiler.parser.other.VisibilityModeParser
import pixel.aurora.compiler.tokenizer.TokenType
import pixel.aurora.compiler.tree.VariableDeclaration
import pixel.aurora.compiler.tree.other.VisibilityMode

class VariableDeclarationParser : Parser<VariableDeclaration>() {

    override fun parse(): VariableDeclaration {
        val annotations = include(AnnotationUsingParser.AnnotationUsingListParser.optional()).getOrElse { emptyList() }
        val visibilityMode = include(VisibilityModeParser().optional()).getOrNull() ?: VisibilityMode.INTERNAL
        val kind = when (buffer.get().expect(TokenType.IDENTIFIER).getRaw()) {
            "let" -> VariableDeclaration.Kind.LET
            "const" -> VariableDeclaration.Kind.CONST
            else -> throw makeError("Unexpected token")
        }
        val name = include(IdentifierParser())
        val type = include(
            parser {
                buffer.get().expectPunctuation(':')
                include(TypeParser())
            }.optional()
        )
        buffer.get().expectPunctuation('=')
        val init = include(ExpressionParser().optional())
        buffer.get().expectPunctuation(';')
        return VariableDeclaration(
            kind,
            name,
            type.getOrNull(),
            init.getOrNull(),
            visibilityMode = visibilityMode,
            annotations = annotations
        )
    }

}