package pixel.aurora.parser.type

import pixel.aurora.parser.Parser
import pixel.aurora.parser.TypeParser
import pixel.aurora.parser.buffer
import pixel.aurora.parser.include
import pixel.aurora.tokenizer.TokenType
import pixel.aurora.tree.Type

class IndependentTypeParser : Parser<Type>() {

    override fun parse(): Type {
        buffer.get().expect("(").expect(TokenType.PUNCTUATION)
        val type = include(TypeParser())
        buffer.get().expect(")").expect(TokenType.PUNCTUATION)
        return type
    }

}
