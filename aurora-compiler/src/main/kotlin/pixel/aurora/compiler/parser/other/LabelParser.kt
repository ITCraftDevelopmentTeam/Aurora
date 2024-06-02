package pixel.aurora.compiler.parser.other

import pixel.aurora.compiler.parser.Parser
import pixel.aurora.compiler.parser.buffer
import pixel.aurora.compiler.parser.expression.IdentifierParser
import pixel.aurora.compiler.parser.include
import pixel.aurora.compiler.tree.Identifier

class LabelParser(private val left: Boolean = true) : Parser<Identifier>() {

    override fun parse(): Identifier {
        return if (left) buffer.get().expectPunctuation('@').let {
            include(IdentifierParser())
        } else include(IdentifierParser()).also {
            buffer.get().expectPunctuation('@')
        }
    }

}