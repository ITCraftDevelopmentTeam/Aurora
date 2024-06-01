package pixel.aurora.parser.other

import pixel.aurora.parser.Parser
import pixel.aurora.parser.TypeParser
import pixel.aurora.parser.buffer
import pixel.aurora.parser.expression.IdentifierParser
import pixel.aurora.parser.include
import pixel.aurora.tokenizer.TokenType
import pixel.aurora.tree.other.Parameter

class ParameterParser : Parser<Parameter>() {

    override fun parse(): Parameter {
        val name = include(IdentifierParser())
        buffer.get().expect(":").expect(TokenType.PUNCTUATION)
        val type = include(TypeParser())
        return Parameter(name, type)
    }

}