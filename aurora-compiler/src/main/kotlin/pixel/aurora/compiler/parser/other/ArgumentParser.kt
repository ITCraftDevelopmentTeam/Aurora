package pixel.aurora.compiler.parser.other

import pixel.aurora.compiler.parser.ExpressionParser
import pixel.aurora.compiler.parser.Parser
import pixel.aurora.compiler.parser.buffer
import pixel.aurora.compiler.parser.expression.IdentifierParser
import pixel.aurora.compiler.parser.include
import pixel.aurora.compiler.tokenizer.TokenType
import pixel.aurora.compiler.tree.other.Argument
import pixel.aurora.compiler.tree.other.NamedArgument

class ArgumentParser : Parser<Argument>() {

    override fun parse(): Argument {
        return Argument(include(ExpressionParser()))
    }

}

class NamedArgumentParser : Parser<NamedArgument>() {

    override fun parse(): NamedArgument {
        val name = include(IdentifierParser())
        buffer.get().expect("=").expect(TokenType.PUNCTUATION)
        val value = include(ExpressionParser())
        return NamedArgument(name, value)
    }

}
