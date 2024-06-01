package pixel.aurora.parser.expression

import pixel.aurora.parser.Parser
import pixel.aurora.parser.buffer
import pixel.aurora.tokenizer.TokenType
import pixel.aurora.tree.Identifier
import pixel.aurora.tree.ThisExpression

class IdentifierParser : Parser<Identifier>() {

    override fun parse(): Identifier {
        return when (val raw = buffer.get().expect(TokenType.IDENTIFIER).getRaw()) {
            "this" -> ThisExpression
            else -> Identifier(raw)
        }
    }

}
