package pixel.aurora.compiler.parser.expression

import pixel.aurora.compiler.parser.Parser
import pixel.aurora.compiler.parser.buffer
import pixel.aurora.compiler.parser.include
import pixel.aurora.compiler.tree.MemberReferenceExpression

class MemberReferenceExpressionParser : Parser<MemberReferenceExpression>() {

    override fun parse(): MemberReferenceExpression {
        repeat(2) {
            buffer.get().expectPunctuation(':')
        }
        val member = include(IdentifierParser())
        return MemberReferenceExpression(member)
    }

}