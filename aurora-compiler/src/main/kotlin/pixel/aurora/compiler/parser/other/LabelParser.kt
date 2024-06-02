package pixel.aurora.compiler.parser.other

import pixel.aurora.compiler.parser.Parser
import pixel.aurora.compiler.parser.buffer
import pixel.aurora.compiler.parser.expression.IdentifierParser
import pixel.aurora.compiler.parser.include
import pixel.aurora.compiler.tokenizer.TokenType
import pixel.aurora.compiler.tree.Identifier

class LabelParser : Parser<Identifier>() {

    override fun parse(): Identifier {
        buffer.get().expect("@").expect(TokenType.PUNCTUATION)
        return include(IdentifierParser())
    }

}