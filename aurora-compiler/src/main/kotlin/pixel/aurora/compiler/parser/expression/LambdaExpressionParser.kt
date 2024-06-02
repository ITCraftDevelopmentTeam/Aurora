package pixel.aurora.compiler.parser.expression

import pixel.aurora.compiler.parser.*
import pixel.aurora.compiler.parser.other.AnnotationUsingParser
import pixel.aurora.compiler.parser.other.ParameterParser
import pixel.aurora.compiler.parser.util.ListParser
import pixel.aurora.compiler.tokenizer.TokenType
import pixel.aurora.compiler.tree.LambdaExpression

class LambdaExpressionParser : Parser<LambdaExpression>() {

    override fun parse(): LambdaExpression {
        val annotations = include(AnnotationUsingParser.AnnotationUsingListParser.optional()).getOrElse { emptyList() }
        buffer.get().expect("{").expect(TokenType.PUNCTUATION)
        val parameters = include(
            parser {
                val list = include(
                    ListParser(ParameterParser(), prefix = null, suffix = null)
                )
                buffer.get().expect("-").expect(TokenType.PUNCTUATION)
                buffer.get().expect(">").expect(TokenType.PUNCTUATION)
                list
            }.optional()
        ).getOrNull() ?: emptyList()
        val body = include(ListParser(StatementParser(), null, null, separator = null))
        buffer.get().expect("}").expect(TokenType.PUNCTUATION)
        return LambdaExpression(parameters, body, annotations)
    }

}