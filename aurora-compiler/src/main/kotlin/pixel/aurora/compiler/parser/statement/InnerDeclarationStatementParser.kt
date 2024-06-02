package pixel.aurora.compiler.parser.statement

import pixel.aurora.compiler.parser.CommonDeclarationParser
import pixel.aurora.compiler.parser.Parser
import pixel.aurora.compiler.parser.include
import pixel.aurora.compiler.tree.InnerDeclarationStatement

class InnerDeclarationStatementParser : Parser<InnerDeclarationStatement>() {

    override fun parse(): InnerDeclarationStatement {
        return InnerDeclarationStatement(include(CommonDeclarationParser()))
    }

}
