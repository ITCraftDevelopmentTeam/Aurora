package pixel.aurora.compiler.parser.declaration

import pixel.aurora.compiler.parser.Parser
import pixel.aurora.compiler.parser.buffer
import pixel.aurora.compiler.tokenizer.TokenType
import pixel.aurora.compiler.tree.Declaration
import pixel.aurora.compiler.tree.EmptyDeclaration

object EmptyDeclarationParser : Parser<Declaration>() {

    override fun parse(): Declaration {
        val got = buffer.get()
        if (got.getTokenType() == TokenType.PUNCTUATION && got.getRaw() == ";") return EmptyDeclaration
        throw makeError("Invalid syntax")
    }

}