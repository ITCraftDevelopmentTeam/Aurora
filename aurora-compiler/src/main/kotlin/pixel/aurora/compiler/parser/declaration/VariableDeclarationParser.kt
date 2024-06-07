package pixel.aurora.compiler.parser.declaration

import pixel.aurora.compiler.parser.*
import pixel.aurora.compiler.parser.expression.IdentifierParser
import pixel.aurora.compiler.parser.other.AnnotationUsingParser
import pixel.aurora.compiler.parser.other.VisibilityModeParser
import pixel.aurora.compiler.parser.util.ListParser
import pixel.aurora.compiler.tokenizer.TokenType
import pixel.aurora.compiler.tree.VariableDeclaration
import pixel.aurora.compiler.tree.other.VisibilityMode

class VariableDeclarationParser(private val mode: Mode = Mode.DEFAULT) : Parser<VariableDeclaration>() {

    enum class Mode {
        MUST_INIT, NO_INIT, DEFAULT
    }

    override fun parse(): VariableDeclaration {
        val annotations = include(ListParser(AnnotationUsingParser(), "@[", "]", ",").optional()).getOrElse { emptyList() }
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
        val init = include(
            parser {
                buffer.get().expectPunctuation('=')
                if (mode == Mode.NO_INIT) throw makeError("Unexpected token ‘=’")
                include(ExpressionParser())
            }.optional()
        )
        if (mode == Mode.MUST_INIT && init.getOrNull() == null)
            throw makeError("Expect '='")
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