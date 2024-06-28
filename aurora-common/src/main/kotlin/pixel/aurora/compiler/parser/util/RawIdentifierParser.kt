package pixel.aurora.compiler.parser.util

import pixel.aurora.compiler.parser.Parser
import pixel.aurora.compiler.parser.buffer
import pixel.aurora.compiler.tokenizer.TokenType

class RawIdentifierParser(private val identifier: String? = null) : Parser<String>() {

    override fun parse(): String {
        return if (identifier != null) buffer.get().expectIdentifier(identifier).getRaw()
        else buffer.get().expect(TokenType.IDENTIFIER).getRaw()
    }

}