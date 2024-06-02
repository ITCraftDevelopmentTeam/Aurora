package pixel.aurora.compiler.parser.declaration

import pixel.aurora.compiler.parser.Parser
import pixel.aurora.compiler.parser.buffer
import pixel.aurora.compiler.tree.ClassDeclaration

class ClassDeclarationParser : Parser<ClassDeclaration>() {

    override fun parse(): ClassDeclaration {
        buffer.get().expectIdentifier("class")
        throw UnsupportedOperationException()
    }

}
