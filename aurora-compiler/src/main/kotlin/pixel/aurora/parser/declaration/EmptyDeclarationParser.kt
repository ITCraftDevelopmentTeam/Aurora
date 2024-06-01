package pixel.aurora.parser.declaration

import pixel.aurora.parser.Parser
import pixel.aurora.parser.buffer
import pixel.aurora.tokenizer.TokenType
import pixel.aurora.tree.Declaration
import pixel.aurora.tree.EmptyDeclaration

object EmptyDeclarationParser : Parser<Declaration>() {

    override fun parse(): Declaration {
        val got = buffer.get()
        if (got.getTokenType() == TokenType.PUNCTUATION && got.getRaw() == ";") return EmptyDeclaration
        throw makeError("Invalid syntax")
    }

}