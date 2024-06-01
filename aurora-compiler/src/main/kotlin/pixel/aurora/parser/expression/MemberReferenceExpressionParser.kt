package pixel.aurora.parser.expression

import pixel.aurora.parser.Parser
import pixel.aurora.parser.buffer
import pixel.aurora.parser.include
import pixel.aurora.tokenizer.TokenType
import pixel.aurora.tree.MemberReferenceExpression

class MemberReferenceExpressionParser : Parser<MemberReferenceExpression>() {

    override fun parse(): MemberReferenceExpression {
        repeat(2) {
            buffer.get().expect(TokenType.PUNCTUATION).expect(":")
        }
        val member = include(IdentifierParser())
        return MemberReferenceExpression(member)
    }

}