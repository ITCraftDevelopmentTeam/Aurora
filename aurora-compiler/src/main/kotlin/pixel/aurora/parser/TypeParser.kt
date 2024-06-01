package pixel.aurora.parser

import pixel.aurora.parser.type.FunctionTypeParser
import pixel.aurora.parser.type.IndependentTypeParser
import pixel.aurora.parser.type.SimpleTypeParser
import pixel.aurora.tokenizer.TokenType
import pixel.aurora.tree.NullableType
import pixel.aurora.tree.Type

class TypeParser : Parser<Type>() {

    fun nullablePart(base: Type) = parser {
        if (base is NullableType) throw makeError("Unexpected '?'")
        buffer.get().expect("?").expect(TokenType.PUNCTUATION)
        NullableType(base)
    }

    fun base() = parser {
        include(IndependentTypeParser() or SimpleTypeParser() or FunctionTypeParser())
    }

    fun part(base: Type) = parser {
        include(nullablePart(base))
    }

    override fun parse(): Type {
        var base = include(base())
        while (true) {
            val result = include(part(base).optional())
            if (result.getOrNull() == null) break
            else base = result.getOrNull()!!
        }
        return base
    }

}