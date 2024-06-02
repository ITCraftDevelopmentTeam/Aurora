package pixel.aurora.compiler.parser.expression

import pixel.aurora.compiler.parser.*
import pixel.aurora.compiler.parser.other.AnnotationUsingParser
import pixel.aurora.compiler.parser.other.ExtendPartParser
import pixel.aurora.compiler.parser.util.ListParser
import pixel.aurora.compiler.tree.ObjectExpression

class ObjectExpressionParser : Parser<ObjectExpression>() {

    override fun parse(): ObjectExpression {
        val annotations = include(AnnotationUsingParser.AnnotationUsingListParser.optional()).getOrElse { emptyList() }
        buffer.get().expectIdentifier("object")
        val extends = include(ExtendPartParser())
        val body = include(ListParser(TopLevelDeclarationParser(), "{", "}", null).optional()).getOrElse { emptyList() }
        return ObjectExpression(extends.second, extends.first, body, annotations)
    }

}