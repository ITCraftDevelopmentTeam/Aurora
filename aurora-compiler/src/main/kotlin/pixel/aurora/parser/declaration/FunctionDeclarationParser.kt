package pixel.aurora.parser.declaration

import pixel.aurora.parser.*
import pixel.aurora.parser.expression.IdentifierParser
import pixel.aurora.parser.other.ParameterParser
import pixel.aurora.parser.other.TypeParameterParser
import pixel.aurora.parser.other.VisibilityModeParser
import pixel.aurora.parser.util.ListParser
import pixel.aurora.tokenizer.TokenType
import pixel.aurora.tree.*
import pixel.aurora.tree.other.Parameter
import pixel.aurora.tree.other.TypeParameter
import pixel.aurora.tree.other.VisibilityMode


class FunctionDeclarationParser : Parser<FunctionDeclaration>() {

    fun typePart() = parser {
        buffer.get().expect(":").expect(TokenType.PUNCTUATION)
        include(TypeParser())
    }

    override fun parse(): FunctionDeclaration {
        val visibilityMode = include(VisibilityModeParser().optional()).getOrNull() ?: VisibilityMode.PUBLIC
        buffer.get().expect(TokenType.IDENTIFIER).expect("function")
        val typeParameters = include(ListParser(TypeParameterParser(), '<', '>').optional()).getOrNull() ?: emptyList()
        val name = include(IdentifierParser())
        val parameters = include(ListParser(ParameterParser()))
        val returnType = include(typePart().optional()).getOrNull() ?: SimpleType.None
        return include(
            expressionFunction(visibilityMode, typeParameters, name, parameters, returnType) or
                    blockFunction(visibilityMode, typeParameters, name, parameters, returnType)
        )
    }

    fun expressionFunction(
        visibilityMode: VisibilityMode,
        typeParameters: List<TypeParameter>,
        name: Identifier,
        parameters: List<Parameter>,
        returnType: Type
    ) = parser {
        buffer.get().expect(TokenType.PUNCTUATION).expect("=")
        val expression = include(ExpressionParser())
        buffer.get().expect(TokenType.PUNCTUATION).expect(";")
        ExpressionFunctionDeclaration(
            name,
            typeParameters,
            parameters,
            returnType,
            expression,
            visibilityMode = visibilityMode
        )
    }

    fun blockFunction(
        visibilityMode: VisibilityMode,
        typeParameters: List<TypeParameter>,
        name: Identifier,
        parameters: List<Parameter>,
        returnType: Type
    ) = parser {
        val block = include(ListParser(StatementParser(), '{', '}', null))
        BlockFunctionDeclaration(name, typeParameters, parameters, returnType, block, visibilityMode = visibilityMode)
    }


}