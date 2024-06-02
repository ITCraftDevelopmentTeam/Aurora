package pixel.aurora.compiler.parser.type

import pixel.aurora.compiler.parser.*
import pixel.aurora.compiler.parser.util.ListParser
import pixel.aurora.compiler.tokenizer.Token
import pixel.aurora.compiler.tokenizer.TokenType
import pixel.aurora.compiler.tokenizer.isRaw
import pixel.aurora.compiler.tree.SimpleType

class SimpleTypeParser : Parser<SimpleType>() {

    override fun parse(): SimpleType {
        var last: Token = buffer.get().expect(TokenType.IDENTIFIER)
        var name = last.getRaw()
        while (true) {
            val position = buffer.position()
            val current = buffer.get()
            name += if (current.isRaw(".") && current.getTokenType() == TokenType.PUNCTUATION && last.getTokenType() == TokenType.IDENTIFIER) "."
            else if (current.getTokenType() == TokenType.IDENTIFIER) current.getRaw()
            else {
                buffer.position(position)
                break
            }
            last = current
        }
        val typeParameters = include(
            ListParser(TypeParser(), "<", ">").optional()
        )
        return SimpleType(name, typeParameters.getOrNull() ?: emptyList())
    }

}