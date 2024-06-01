package pixel.aurora.parser.declaration

import pixel.aurora.parser.*
import pixel.aurora.tokenizer.TokenType
import pixel.aurora.tree.ExpressionFunctionDeclaration
import pixel.aurora.tree.FunctionDeclaration
import pixel.aurora.tree.Identifier

class ExpressionFunctionDeclarationParser : Parser<ExpressionFunctionDeclaration>() {

    override fun parse(): ExpressionFunctionDeclaration {
        buffer.get().expect(TokenType.IDENTIFIER).expect("fun")
        val name = buffer.get().expect(TokenType.IDENTIFIER)
        buffer.get().expect(TokenType.PUNCTUATION).expect("=")
        val expression = include(ExpressionParser())
        buffer.get().expect(TokenType.PUNCTUATION).expect(";")
        return ExpressionFunctionDeclaration(Identifier(name.getRaw()), expression)
    }

}

class FunctionDeclarationParser : Parser<FunctionDeclaration>() {

    override fun parse(): FunctionDeclaration {
        return include(choose(ExpressionFunctionDeclarationParser()))
    }

}