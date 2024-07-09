package pixel.aurora.core.parser.util

import pixel.aurora.core.parser.Parser
import pixel.aurora.core.parser.buffer
import pixel.aurora.core.tokenizer.IdentifierToken
import pixel.aurora.core.tokenizer.expect
import pixel.aurora.core.tokenizer.expectIdentifier

class RawIdentifierParser(private val identifier: String? = null) : Parser<IdentifierToken>() {

    override fun parse(): IdentifierToken {
        return if (identifier != null) buffer.get().expectIdentifier(identifier)
        else buffer.get().expect<IdentifierToken>()
    }

}