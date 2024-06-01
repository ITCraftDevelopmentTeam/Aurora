package pixel.aurora.parser.type

import pixel.aurora.parser.*
import pixel.aurora.parser.other.ParameterParser
import pixel.aurora.parser.util.ListParser
import pixel.aurora.tokenizer.TokenType
import pixel.aurora.tree.FunctionType

class FunctionTypeParser : Parser<FunctionType>() {

    override fun parse(): FunctionType {
        val parameters = include(ListParser(ParameterParser()))
        val thisType = include(
            parser {
                buffer.get().expect(":").expect(TokenType.PUNCTUATION)
                include(TypeParser())
            }.optional()
        )
        buffer.get().expect("-").expect(TokenType.PUNCTUATION)
        buffer.get().expect(">").expect(TokenType.PUNCTUATION)
        val returnType = include(TypeParser())
        return FunctionType(parameters, returnType, thisType.getOrNull())
    }

}