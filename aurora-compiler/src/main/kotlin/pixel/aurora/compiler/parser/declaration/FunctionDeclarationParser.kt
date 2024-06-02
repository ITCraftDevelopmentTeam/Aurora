package pixel.aurora.compiler.parser.declaration

import pixel.aurora.compiler.parser.*
import pixel.aurora.compiler.parser.expression.IdentifierParser
import pixel.aurora.compiler.parser.other.AnnotationUsingParser
import pixel.aurora.compiler.parser.other.ParameterParser
import pixel.aurora.compiler.parser.other.TypeParameterParser
import pixel.aurora.compiler.parser.other.VisibilityModeParser
import pixel.aurora.compiler.parser.util.ListParser
import pixel.aurora.compiler.tokenizer.TokenType
import pixel.aurora.compiler.tree.*
import pixel.aurora.compiler.tree.other.AnnotationUsing
import pixel.aurora.compiler.tree.other.Parameter
import pixel.aurora.compiler.tree.other.TypeParameter
import pixel.aurora.compiler.tree.other.VisibilityMode


class FunctionDeclarationParser : Parser<FunctionDeclaration>() {

    fun typePart() = parser {
        buffer.get().expect(":").expect(TokenType.PUNCTUATION)
        include(TypeParser())
    }

    override fun parse(): FunctionDeclaration {
        val annotations =
            include(ListParser(AnnotationUsingParser(), "@[", "]", ",").optional()).getOrElse { emptyList() }
        val visibilityMode = include(VisibilityModeParser().optional()).getOrNull() ?: VisibilityMode.PUBLIC
        buffer.get().expect(TokenType.IDENTIFIER).expect("function")
        val typeParameters = include(ListParser(TypeParameterParser(), "<", ">").optional()).getOrNull() ?: emptyList()
        val name = include(IdentifierParser())
        val parameters = include(ListParser(ParameterParser()))
        val returnType = include(typePart().optional()).getOrNull() ?: SimpleType.None
        return include(
            expressionFunction(visibilityMode, typeParameters, name, parameters, returnType, annotations) or
                    blockFunction(visibilityMode, typeParameters, name, parameters, returnType, annotations) or
                    parser {
                        EmptyFunctionDeclaration(
                            name,
                            typeParameters,
                            parameters,
                            returnType,
                            visibilityMode,
                            annotations
                        )
                    }
        )
    }

    fun expressionFunction(
        visibilityMode: VisibilityMode,
        typeParameters: List<TypeParameter>,
        name: Identifier,
        parameters: List<Parameter>,
        returnType: Type,
        annotations: List<AnnotationUsing>
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
            visibilityMode = visibilityMode,
            annotations = annotations
        )
    }

    fun blockFunction(
        visibilityMode: VisibilityMode,
        typeParameters: List<TypeParameter>,
        name: Identifier,
        parameters: List<Parameter>,
        returnType: Type,
        annotations: List<AnnotationUsing>
    ) = parser {
        val block = include(ListParser(StatementParser(), "{", "}", null))
        BlockFunctionDeclaration(
            name,
            typeParameters,
            parameters,
            returnType,
            block,
            visibilityMode = visibilityMode,
            annotations = annotations
        )
    }


}