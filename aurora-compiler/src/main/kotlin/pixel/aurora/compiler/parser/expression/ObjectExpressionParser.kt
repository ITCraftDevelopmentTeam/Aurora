package pixel.aurora.compiler.parser.expression

import pixel.aurora.compiler.parser.*
import pixel.aurora.compiler.parser.other.AnnotationUsingParser
import pixel.aurora.compiler.parser.other.ExtendPartParser
import pixel.aurora.compiler.parser.util.ListParser
import pixel.aurora.compiler.tree.ObjectExpression
import pixel.aurora.compiler.tree.SimpleType
import pixel.aurora.compiler.tree.other.ClassCall

class ObjectExpressionParser : Parser<ObjectExpression>() {

    override fun parse(): ObjectExpression {
        val annotations = include(ListParser(AnnotationUsingParser(), "@[", "]", ",").optional()).getOrElse { emptyList() }
        buffer.get().expectIdentifier("object")
        val extends = include(ExtendPartParser().optional()).getOrNull()
        val body = include(ListParser(TopLevelDeclarationParser(), "{", "}", null).optional()).getOrElse { emptyList() }
        return ObjectExpression(
            extends?.second ?: emptyList(),
            extends?.first ?: ClassCall(SimpleType.Any, emptyList()),
            body,
            annotations
        )
    }

}