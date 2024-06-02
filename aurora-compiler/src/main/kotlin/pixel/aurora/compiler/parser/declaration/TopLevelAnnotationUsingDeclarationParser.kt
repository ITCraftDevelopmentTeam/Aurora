package pixel.aurora.compiler.parser.declaration

import pixel.aurora.compiler.parser.Parser
import pixel.aurora.compiler.parser.include
import pixel.aurora.compiler.parser.other.AnnotationUsingParser
import pixel.aurora.compiler.parser.util.ListParser
import pixel.aurora.compiler.tree.other.AnnotationUsing

class TopLevelAnnotationUsingDeclarationParser : Parser<List<AnnotationUsing>>() {

    override fun parse(): List<AnnotationUsing> {
        return include(ListParser(AnnotationUsingParser(), "@[", "];", ","))
    }

}