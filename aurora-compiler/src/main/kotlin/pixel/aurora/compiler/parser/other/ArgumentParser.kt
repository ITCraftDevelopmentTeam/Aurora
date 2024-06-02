package pixel.aurora.compiler.parser.other

import pixel.aurora.compiler.parser.*
import pixel.aurora.compiler.parser.expression.IdentifierParser
import pixel.aurora.compiler.tree.other.Argument
import pixel.aurora.compiler.tree.other.NamedArgument

class ArgumentParser : Parser<Argument>() {

    override fun parse(): Argument {
        val isRest = include(
            parser {
                buffer.get().expectPunctuation('*')
            }.optional()
        ).getOrNull() != null
        val expression = include(ExpressionParser())
        return Argument(expression, isRest)
    }

}

class NamedArgumentParser : Parser<NamedArgument>() {

    override fun parse(): NamedArgument {
        val name = include(IdentifierParser())
        buffer.get().expectPunctuation('=')
        val value = include(ExpressionParser())
        return NamedArgument(name, value)
    }

}
