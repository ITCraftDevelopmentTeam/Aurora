package pixel.aurora.parser.other

import pixel.aurora.parser.*
import pixel.aurora.parser.expression.IdentifierParser
import pixel.aurora.tokenizer.TokenType
import pixel.aurora.tree.other.TypeParameter

class TypeParameterParser : Parser<TypeParameter>() {

    override fun parse(): TypeParameter {
        val name = include(IdentifierParser())
        val type = include(
            parser {
                buffer.get().expect(":").expect(TokenType.PUNCTUATION)
                include(TypeParser())
            }.optional()
        )
        return TypeParameter(name, type.getOrNull())
    }

}