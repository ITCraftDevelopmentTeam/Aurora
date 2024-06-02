package pixel.aurora.compiler.parser.declaration

import pixel.aurora.compiler.parser.Parser
import pixel.aurora.compiler.parser.buffer
import pixel.aurora.compiler.tokenizer.Token
import pixel.aurora.compiler.tokenizer.TokenType
import pixel.aurora.compiler.tokenizer.isToken
import pixel.aurora.compiler.tree.Program

class ImportDeclarationParser : Parser<Program.ImportDeclaration>() {

    override fun parse(): Program.ImportDeclaration {
        buffer.get().expectIdentifier("import")
        var last: Token = buffer.get().expect(TokenType.IDENTIFIER)
        var name = last.getRaw()
        var importAll = false
        while (true) {
            val current = buffer.get()
            name += if (current.isToken(";", TokenType.PUNCTUATION)) break
            else if (last.isToken(".", TokenType.PUNCTUATION) && current.isToken("*", TokenType.PUNCTUATION)) {
                importAll = true
                break
            } else if (current.isToken(".", TokenType.PUNCTUATION) && last.isToken(type = TokenType.IDENTIFIER)) "."
            else if (current.isToken(type = TokenType.IDENTIFIER)) current.getRaw()
            else throw makeError("Invalid syntax")
            last = current
        }
        name = name.trimEnd('.')
        return Program.ImportDeclaration(name, importAll)
    }

}