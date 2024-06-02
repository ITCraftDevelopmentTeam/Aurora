package pixel.aurora.compiler.parser.declaration

import pixel.aurora.compiler.parser.*
import pixel.aurora.compiler.parser.expression.IdentifierParser
import pixel.aurora.compiler.parser.other.TypeParameterParser
import pixel.aurora.compiler.parser.other.VisibilityModeParser
import pixel.aurora.compiler.parser.util.ListParser
import pixel.aurora.compiler.tree.*
import pixel.aurora.compiler.tree.other.Argument
import pixel.aurora.compiler.tree.other.Parameter
import pixel.aurora.compiler.tree.other.TypeParameter
import pixel.aurora.compiler.tree.other.VisibilityMode

class TypeDeclarationParser : Parser<Declaration>() {

    fun simpleTypeAlias(name: Identifier, typeParameters: List<TypeParameter>, visibilityMode: VisibilityMode) =
        parser {
            TypeDeclaration(name, typeParameters, include(TypeParser()), visibilityMode)
        }

    fun tupleTypeAlias(name: Identifier, typeParameters: List<TypeParameter>, visibilityMode: VisibilityMode) = parser {
        val tuple = include(ListParser(TypeParser()))
        val getMember = MemberExpression(ThisExpression(), Identifier("internal:operator/get"))
        val methods = tuple.mapIndexed { index, item ->
            ExpressionFunctionDeclaration(
                Identifier("component$index"), emptyList(), emptyList(), item, CallExpression(
                    getMember, listOf(
                        Argument(NumberLiteral(index.toBigDecimal()))
                    ), emptyList()
                ), VisibilityMode.PUBLIC, emptyList()
            )
        }
        val body = mutableListOf<Declaration>()
        body += methods
        body += EmptyFunctionDeclaration(
            Identifier("internal:operator/get"),
            emptyList(),
            listOf(Parameter(Identifier("index"), SimpleType.Number)),
            SimpleType.Any,
            VisibilityMode.PUBLIC,
            emptyList()
        )
        val interfaceDeclaration = InterfaceDeclaration(name, typeParameters, emptyList(), body, visibilityMode)
        return@parser interfaceDeclaration
    }

    override fun parse(): Declaration {
        val visibilityMode = include(VisibilityModeParser().optional()).getOrElse { VisibilityMode.PUBLIC }
        buffer.get().expectIdentifier("type")
        val name = include(IdentifierParser())
        val typeParameters = include(ListParser(TypeParameterParser(), "<", ">").optional()).getOrElse { emptyList() }
        buffer.get().expectPunctuation('=')
        val result = include(
            tupleTypeAlias(name, typeParameters, visibilityMode) or simpleTypeAlias(
                name,
                typeParameters,
                visibilityMode
            )
        )
        buffer.get().expectPunctuation(';')
        return result
    }

}