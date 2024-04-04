package pixel.aurora.parser

import pixel.aurora.tree.FunctionDeclaration
import pixel.aurora.tree.CommentsDeclaration

class CommentsDeclarationParser : Parser<CommentsDeclaration>() {

    override fun parse(): CommentsDeclaration {
        val buffer = getBuffer()
        include(whitespace())
        if (buffer.startsWith("//")) {
            buffer.position(buffer.position() + 2)
            var current = buffer.get()
            var text = current.toString()
            while (current !in "\n\r") {
                current = buffer.get()
                text += current
            }
            include(whitespace())
            return CommentsDeclaration(text, false)
        } else if (buffer.startsWith("/*")) {
            buffer.position(buffer.position() + 2)
            var text = ""
            while (true) {
                val got = buffer.get()
                if (text.endsWith("*") && got == '/') break
                text += got
            }
            text = text.substring(0 .. text.length - 2)
            include(whitespace())
            return CommentsDeclaration(text, true)
        }
        throw makeError(ParserMessages.invalidSyntax)
    }

}

class FunctionDeclarationParser : Parser<FunctionDeclaration>() {

    override fun parse(): FunctionDeclaration {
        include(keyword("function"))
        include(whitespace(min = 1))
        val name = include(IdentifierParser())
        include(whitespace())
        include(keyword("() {}"))
        return FunctionDeclaration(name, listOf())
    }

}