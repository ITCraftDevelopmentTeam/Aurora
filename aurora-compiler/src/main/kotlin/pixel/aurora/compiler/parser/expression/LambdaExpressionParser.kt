package pixel.aurora.compiler.parser.expression

import pixel.aurora.compiler.parser.*
import pixel.aurora.compiler.parser.other.AnnotationUsingParser
import pixel.aurora.compiler.parser.other.ParameterParser
import pixel.aurora.compiler.parser.util.ListParser
import pixel.aurora.compiler.tree.LambdaExpression

class LambdaExpressionParser : Parser<LambdaExpression>() {

    override fun parse(): LambdaExpression {
        val annotations = include(ListParser(AnnotationUsingParser(), "@[", "]", ",").optional()).getOrElse { emptyList() }
        buffer.get().expectPunctuation('{')
        val parameters = include(
            parser {
                val list = include(
                    ListParser(ParameterParser(), prefix = null, suffix = null)
                )
                buffer.get().expectPunctuation('-')
                buffer.get().expectPunctuation('>')
                list
            }.optional()
        ).getOrNull() ?: emptyList()
        val body = include(ListParser(StatementParser(), null, null, separator = null))
        buffer.get().expectPunctuation('}')
        return LambdaExpression(parameters, body, annotations)
    }

}