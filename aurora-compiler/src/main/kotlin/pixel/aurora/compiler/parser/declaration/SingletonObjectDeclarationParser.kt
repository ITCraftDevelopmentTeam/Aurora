package pixel.aurora.compiler.parser.declaration

import pixel.aurora.compiler.parser.*
import pixel.aurora.compiler.parser.expression.IdentifierParser
import pixel.aurora.compiler.parser.other.AnnotationUsingParser
import pixel.aurora.compiler.parser.other.ExtendPartParser
import pixel.aurora.compiler.parser.other.VisibilityModeParser
import pixel.aurora.compiler.parser.util.ListParser
import pixel.aurora.compiler.tree.ObjectExpression
import pixel.aurora.compiler.tree.SingletonObjectDeclaration
import pixel.aurora.compiler.tree.other.VisibilityMode

class SingletonObjectDeclarationParser : Parser<SingletonObjectDeclaration>() {

    override fun parse(): SingletonObjectDeclaration {
        val annotations = include(AnnotationUsingParser.AnnotationUsingListParser.optional()).getOrElse { emptyList() }
        val visibilityMode = include(VisibilityModeParser().optional()).getOrElse { VisibilityMode.PUBLIC }
        buffer.get().expectIdentifier("object")
        val name = include(IdentifierParser())
        val extends = include(ExtendPartParser())
        val body = include(ListParser(TopLevelDeclarationParser(), "{", "}", null).optional()).getOrElse { emptyList() }
        val expression = ObjectExpression(extends.second, extends.first, body, annotations)
        return SingletonObjectDeclaration(name, expression, visibilityMode)
    }

}