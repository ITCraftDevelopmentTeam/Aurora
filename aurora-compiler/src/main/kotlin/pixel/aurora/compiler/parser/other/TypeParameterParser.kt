package pixel.aurora.compiler.parser.other

import pixel.aurora.compiler.parser.*
import pixel.aurora.compiler.parser.expression.IdentifierParser
import pixel.aurora.compiler.tree.other.TypeParameter

class TypeParameterParser : Parser<TypeParameter>() {

    override fun parse(): TypeParameter {
        val name = include(IdentifierParser())
        val type = include(
            parser {
                buffer.get().expectPunctuation(':')
                include(TypeParser())
            }.optional()
        )
        return TypeParameter(name, type.getOrNull())
    }

}