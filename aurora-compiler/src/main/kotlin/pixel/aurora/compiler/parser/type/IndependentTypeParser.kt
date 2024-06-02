package pixel.aurora.compiler.parser.type

import pixel.aurora.compiler.parser.Parser
import pixel.aurora.compiler.parser.TypeParser
import pixel.aurora.compiler.parser.buffer
import pixel.aurora.compiler.parser.include
import pixel.aurora.compiler.tree.Type

class IndependentTypeParser : Parser<Type>() {

    override fun parse(): Type {
        buffer.get().expectPunctuation('(')
        val type = include(TypeParser())
        buffer.get().expectPunctuation(')')
        return type
    }

}
