package pixel.aurora.parser.expression

import pixel.aurora.parser.Parser
import pixel.aurora.parser.buffer
import pixel.aurora.tokenizer.TokenType
import pixel.aurora.tree.Identifier

class IdentifierExpressionParser : Parser<Identifier>() {

    override fun parse(): Identifier {
        return Identifier(buffer.get().expect(TokenType.IDENTIFIER).getRaw())
    }

}
