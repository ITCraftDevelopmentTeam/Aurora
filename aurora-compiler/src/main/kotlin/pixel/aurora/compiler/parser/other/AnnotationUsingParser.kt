package pixel.aurora.compiler.parser.other

import pixel.aurora.compiler.parser.*
import pixel.aurora.compiler.parser.expression.IdentifierParser
import pixel.aurora.compiler.parser.type.SimpleTypeParser
import pixel.aurora.compiler.parser.util.ListParser
import pixel.aurora.compiler.tree.other.AnnotationUsing

class AnnotationUsingParser : Parser<AnnotationUsing>() {

    object AnnotationUsingListParser : Parser<List<AnnotationUsing>>() {

        override fun parse(): List<AnnotationUsing> {
            return include(ListParser(AnnotationUsingParser(), "@[", "]"))
        }

    }

    fun targetPart() = parser {
        val name = include(IdentifierParser()).getIdentifierName()
        buffer.get().expectPunctuation(':')
        return@parser AnnotationUsing.Target.entries.first { it.symbol == name }
    }

    override fun parse(): AnnotationUsing {
        val target = include(targetPart().optional()).getOrNull()
        val annotation = include(SimpleTypeParser())
        val arguments = include(
            ListParser(NamedArgumentParser() or ArgumentParser()).optional()
        ).getOrNull() ?: emptyList()
        return AnnotationUsing(annotation, target, arguments)
    }

}