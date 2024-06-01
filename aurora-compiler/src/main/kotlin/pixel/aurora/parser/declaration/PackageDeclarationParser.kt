package pixel.aurora.parser.declaration

import pixel.aurora.parser.Parser
import pixel.aurora.parser.buffer
import pixel.aurora.tokenizer.Token
import pixel.aurora.tokenizer.TokenType
import pixel.aurora.tokenizer.isRaw
import pixel.aurora.tree.Program

class PackageDeclarationParser : Parser<Program.PackageDeclaration>() {

    override fun parse(): Program.PackageDeclaration {
        buffer.get().expect(TokenType.IDENTIFIER).expect("package")
        var last: Token = buffer.get().expect(TokenType.IDENTIFIER)
        var name = last.getRaw()
        while (true) {
            val current = buffer.get()
            name += if (current.isRaw(";") && current.getTokenType() == TokenType.PUNCTUATION) break
            else if (current.isRaw(".") && current.getTokenType() == TokenType.PUNCTUATION && last.getTokenType() == TokenType.IDENTIFIER) "."
            else if (current.getTokenType() == TokenType.IDENTIFIER) current.getRaw()
            else throw makeError("Invalid syntax")
            last = current
        }
        return Program.PackageDeclaration(name)
    }

}