package pixel.aurora.compiler.parser.type

import pixel.aurora.compiler.parser.*
import pixel.aurora.compiler.parser.other.ParameterParser
import pixel.aurora.compiler.parser.util.ListParser
import pixel.aurora.compiler.tree.FunctionType

class FunctionTypeParser : Parser<FunctionType>() {

    override fun parse(): FunctionType {
        val parameters = include(ListParser(ParameterParser()))
        val thisType = include(
            parser {
                buffer.get().expectPunctuation(':')
                include(TypeParser())
            }.optional()
        )
        buffer.get().expectPunctuation('-')
        buffer.get().expectPunctuation('>')
        val returnType = include(TypeParser())
        return FunctionType(parameters, returnType, thisType.getOrNull())
    }

}