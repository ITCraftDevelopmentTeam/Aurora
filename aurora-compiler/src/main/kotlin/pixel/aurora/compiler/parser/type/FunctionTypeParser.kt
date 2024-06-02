package pixel.aurora.compiler.parser.type

import pixel.aurora.compiler.parser.*
import pixel.aurora.compiler.parser.other.ParameterParser
import pixel.aurora.compiler.parser.util.ListParser
import pixel.aurora.compiler.tokenizer.TokenType
import pixel.aurora.compiler.tree.FunctionType

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