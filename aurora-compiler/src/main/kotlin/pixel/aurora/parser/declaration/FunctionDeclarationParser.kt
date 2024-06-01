package pixel.aurora.parser.declaration

import pixel.aurora.parser.*
import pixel.aurora.parser.expression.IdentifierExpressionParser
import pixel.aurora.parser.other.ParameterParser
import pixel.aurora.parser.util.ListParser
import pixel.aurora.tokenizer.TokenType
import pixel.aurora.tree.*

class ExpressionFunctionDeclarationParser : Parser<ExpressionFunctionDeclaration>() {

    fun typePart() = parser {
        buffer.get().expect(":").expect(TokenType.PUNCTUATION)
        include(TypeParser())
    }

    override fun parse(): ExpressionFunctionDeclaration {
        buffer.get().expect(TokenType.IDENTIFIER).expect("fun")
        val name = include(IdentifierExpressionParser())
        val parameters = include(ListParser(ParameterParser()))
        val returnType = include(typePart().optional()).getOrNull() ?: SimpleType.None
        buffer.get().expect(TokenType.PUNCTUATION).expect("=")
        val expression = include(ExpressionParser())
        buffer.get().expect(TokenType.PUNCTUATION).expect(";")
        return ExpressionFunctionDeclaration(name, parameters, returnType, expression)
    }

}

class BlockFunctionDeclarationParser : Parser<BlockFunctionDeclaration>() {

    fun typePart() = parser {
        buffer.get().expect(":").expect(TokenType.PUNCTUATION)
        include(TypeParser())
    }

    override fun parse(): BlockFunctionDeclaration {
        buffer.get().expect(TokenType.IDENTIFIER).expect("fun")
        val name = include(IdentifierExpressionParser())
        val parameters = include(ListParser(ParameterParser()))
        val returnType = include(typePart().optional()).getOrNull() ?: SimpleType.None
        val block = include(ListParser(StatementParser(), '{', '}', null))
        return BlockFunctionDeclaration(name, parameters, returnType, block)
    }

}

class FunctionDeclarationParser : Parser<FunctionDeclaration>() {

    override fun parse(): FunctionDeclaration {
        return include(choose(ExpressionFunctionDeclarationParser(), BlockFunctionDeclarationParser()))
    }

}