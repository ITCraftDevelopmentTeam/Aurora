package pixel.aurora.compiler.parser.expression

import pixel.aurora.compiler.parser.Parser
import pixel.aurora.compiler.parser.buffer
import pixel.aurora.compiler.parser.include
import pixel.aurora.compiler.parser.optional
import pixel.aurora.compiler.parser.other.LabelParser
import pixel.aurora.compiler.tokenizer.TokenType
import pixel.aurora.compiler.tree.Identifier
import pixel.aurora.compiler.tree.ThisExpression

class IdentifierParser(private val identifier: String? = null) : Parser<Identifier>() {

    override fun parse(): Identifier {
        return if (identifier != null) Identifier(buffer.get().expectIdentifier(identifier).getRaw())
        else when (val raw = buffer.get().expect(TokenType.IDENTIFIER).getRaw()) {
            "this" -> ThisExpression(include(LabelParser().optional()).getOrNull())
            else -> Identifier(raw)
        }
    }

}