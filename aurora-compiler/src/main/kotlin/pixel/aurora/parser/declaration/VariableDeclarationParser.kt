package pixel.aurora.parser.declaration

import pixel.aurora.parser.*
import pixel.aurora.parser.expression.IdentifierParser
import pixel.aurora.parser.other.VisibilityModeParser
import pixel.aurora.tokenizer.TokenType
import pixel.aurora.tree.VariableDeclaration
import pixel.aurora.tree.other.VisibilityMode

class VariableDeclarationParser : Parser<VariableDeclaration>() {

    override fun parse(): VariableDeclaration {
        val visibilityMode = include(VisibilityModeParser().optional()).getOrNull() ?: VisibilityMode.INTERNAL
        val kind = when (buffer.get().expect(TokenType.IDENTIFIER).getRaw()) {
            "let" -> VariableDeclaration.Kind.LET
            "const" -> VariableDeclaration.Kind.CONST
            else -> throw makeError("Unexpected token")
        }
        val name = include(IdentifierParser())
        val type = include(
            parser {
                buffer.get().expect(TokenType.PUNCTUATION).expect(":")
                include(TypeParser())
            }.optional()
        )
        buffer.get().expect(TokenType.PUNCTUATION).expect("=")
        val init = include(ExpressionParser().optional())
        if (kind == VariableDeclaration.Kind.CONST && init.getOrNull() == null) throw makeError("Missing initializer in const declaration")
        return VariableDeclaration(kind, name, type.getOrNull(), init.getOrNull(), visibilityMode = visibilityMode)
    }

}