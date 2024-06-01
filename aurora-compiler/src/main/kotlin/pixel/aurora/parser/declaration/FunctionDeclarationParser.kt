package pixel.aurora.parser.declaration

import pixel.aurora.parser.*
import pixel.aurora.parser.other.ParameterParser
import pixel.aurora.parser.util.ListParser
import pixel.aurora.tokenizer.TokenType
import pixel.aurora.tree.ExpressionFunctionDeclaration
import pixel.aurora.tree.FunctionDeclaration
import pixel.aurora.tree.Identifier
import pixel.aurora.tree.SimpleType

class ExpressionFunctionDeclarationParser : Parser<ExpressionFunctionDeclaration>() {

    fun typePart() = parser {
        buffer.get().expect(":").expect(TokenType.PUNCTUATION)
        include(TypeParser())
    }

    override fun parse(): ExpressionFunctionDeclaration {
        buffer.get().expect(TokenType.IDENTIFIER).expect("fun")
        val name = buffer.get().expect(TokenType.IDENTIFIER)
        val parameters = include(ListParser(ParameterParser()))
        val returnType = include(typePart().optional()).getOrNull() ?: SimpleType.None
        buffer.get().expect(TokenType.PUNCTUATION).expect("=")
        val expression = include(ExpressionParser())
        buffer.get().expect(TokenType.PUNCTUATION).expect(";")
        return ExpressionFunctionDeclaration(Identifier(name.getRaw()), parameters, returnType, expression)
    }

}

class FunctionDeclarationParser : Parser<FunctionDeclaration>() {

    override fun parse(): FunctionDeclaration {
        return include(choose(ExpressionFunctionDeclarationParser()))
    }

}