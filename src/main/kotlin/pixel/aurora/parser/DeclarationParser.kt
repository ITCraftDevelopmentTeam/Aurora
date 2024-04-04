@file:Suppress("DuplicatedCode")

package pixel.aurora.parser

import pixel.aurora.tree.BlockFunctionDeclaration
import pixel.aurora.tree.CommentsDeclaration
import pixel.aurora.tree.ExpressionFunctionDeclaration

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
            text = text.substring(0..text.length - 2)
            include(whitespace())
            return CommentsDeclaration(text, true)
        }
        throw makeError(ParserMessages.invalidSyntax)
    }

}

class BlockFunctionDeclarationParser : Parser<BlockFunctionDeclaration>() {

    override fun parse(): BlockFunctionDeclaration {
        include(keyword("fun"))
        include(whitespace(min = 1))
        val name = include(IdentifierParser())
        include(whitespace())
        include(keyword("("))
        include(whitespace())
        include(keyword(")"))
        include(whitespace())
        include(keyword("{"))
        val body = repeat(
            parser {
                include(whitespace())
                val statement = include(StatementParser())
                include(semicolon(true))
                statement
            }
        ).let { include(it) }
        include(whitespace())
        include(keyword("}"))
        include(whitespace())
        return BlockFunctionDeclaration(name, body.map { it })
    }

}

class ExpressionFunctionDeclarationParser : Parser<ExpressionFunctionDeclaration>() {

    override fun parse(): ExpressionFunctionDeclaration {
        include(keyword("fun"))
        include(whitespace(min = 1))
        val name = include(IdentifierParser())
        include(whitespace())
        include(keyword("("))
        include(whitespace())
        include(keyword(")"))
        include(whitespace())
        include(keyword("="))
        include(whitespace())
        val expression = include(ExpressionParser())
        include(semicolon())
        return ExpressionFunctionDeclaration(name, expression)
    }

}