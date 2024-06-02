package pixel.aurora.compiler.parser.declaration

import pixel.aurora.compiler.parser.Parser
import pixel.aurora.compiler.parser.buffer
import pixel.aurora.compiler.tokenizer.Token
import pixel.aurora.compiler.tokenizer.TokenType
import pixel.aurora.compiler.tokenizer.isToken
import pixel.aurora.compiler.tree.Program

class PackageDeclarationParser : Parser<Program.PackageDeclaration>() {

    override fun parse(): Program.PackageDeclaration {
        buffer.get().expectIdentifier("package")
        var last: Token = buffer.get().expect(TokenType.IDENTIFIER)
        var name = last.getRaw()
        while (true) {
            val current = buffer.get()
            name += if (current.isToken(";", TokenType.PUNCTUATION)) break
            else if (current.isToken(".", TokenType.PUNCTUATION) && last.getTokenType() == TokenType.IDENTIFIER) "."
            else if (current.getTokenType() == TokenType.IDENTIFIER) current.getRaw()
            else throw makeError("Invalid syntax")
            last = current
        }
        return Program.PackageDeclaration(name)
    }

}