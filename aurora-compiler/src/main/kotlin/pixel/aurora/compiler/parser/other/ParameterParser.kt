package pixel.aurora.compiler.parser.other

import pixel.aurora.compiler.parser.*
import pixel.aurora.compiler.parser.expression.IdentifierParser
import pixel.aurora.compiler.tree.other.Parameter

class ParameterParser : Parser<Parameter>() {

    override fun parse(): Parameter {
        val name = include(IdentifierParser())
        buffer.get().expectPunctuation(':')
        val type = include(TypeParser())
        val default = include(
            parser {
                buffer.get().expectPunctuation('=')
                include(ExpressionParser())
            }.optional()
        )
        return Parameter(name, type, default.getOrNull())
    }

}