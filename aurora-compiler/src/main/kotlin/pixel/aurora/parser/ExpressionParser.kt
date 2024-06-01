package pixel.aurora.parser

import pixel.aurora.parser.expression.*
import pixel.aurora.parser.util.ListParser
import pixel.aurora.parser.util.StringUtils.chunkPunctuationAndIdentifier
import pixel.aurora.tokenizer.TokenType
import pixel.aurora.tree.*

class BinaryExpressionParser(val left: Expression) : Parser<BinaryExpression>() {

    fun operator(operator: String) = parser {
        for (character in operator) buffer.get().expect(character.toString()).expect(TokenType.PUNCTUATION)
        return@parser BinaryExpression(left, include(ExpressionParser()), operator)
    }

    fun keyword(operator: String) = parser {
        for (chunk in chunkPunctuationAndIdentifier(operator)) buffer.get().expect(chunk.first).expect(chunk.second)
        return@parser BinaryExpression(left, include(ExpressionParser()), operator)
    }

    override fun parse(): BinaryExpression {
        return include(
            operator("+") or
                    operator("-") or
                    operator("*") or
                    operator("/") or
                    operator("===") or operator("==") or
                    operator("!===") or operator("!==") or
                    operator("||") or operator("&&") or
                    operator("|") or operator("&") or
                    operator(">=") or operator("<=") or
                    operator(">") or operator("<") or
                    operator("?:") or
                    keyword("in") or keyword("!in")
        )
    }

}

class UpdateExpressionParser(val left: Expression) : Parser<UpdateExpression>() {

    fun operator(operator: String) = parser {
        for (character in operator) {
            buffer.get().expect(character.toString()).expect(TokenType.PUNCTUATION)
        }
        return@parser UpdateExpression(left, operator)
    }

    override fun parse(): UpdateExpression {
        return include(
            operator("++") or operator("--")
        )
    }

}

class AssignmentExpressionParser(val left: Expression) : Parser<AssignmentExpression>() {

    fun operator(operator: String) = parser {
        for (character in operator) {
            buffer.get().expect(character.toString()).expect(TokenType.PUNCTUATION)
        }
        val right = include(ExpressionParser())
        return@parser AssignmentExpression(left, right, operator)
    }

    override fun parse(): AssignmentExpression {
        if (left is Literal<*>) throw makeError("Invalid left-hand side in assignment")
        return include(
            operator(":=") or operator("+=") or operator("-=") or operator("*=") or operator("/=")
        )
    }

}

class ExpressionParser : Parser<Expression>() {

    fun base() =
        LiteralExpressionParser() or IdentifierParser() or IndependentExpressionParser() or UnaryExpressionParser() or ClosureExpressionParser() or MemberReferenceExpressionParser()

    fun part(base: Expression) = parser {
        include(
            memberReferencePart(base) or distinctCastingPart(base) or memberExpressionPart(base) or callExpressionPart(
                base
            ) or AssignmentExpressionParser(base) or BinaryExpressionParser(base) or UpdateExpressionParser(base) or asExpressionPart(
                base
            ) or isExpressionPart(base)
        )
    }

    fun distinctCastingPart(base: Expression) = parser {
        repeat(2) {
            buffer.get().expect("!").expect(TokenType.PUNCTUATION)
        }
        DistinctCastingExpression(base)
    }

    fun isExpressionPart(base: Expression) = parser {
        val isReversed = include(
            parser { buffer.get().expect("!").expect(TokenType.PUNCTUATION) }.optional()
        ).getOrNull() != null
        buffer.get().expect("is").expect(TokenType.IDENTIFIER)
        val type = include(TypeParser())
        return@parser IsExpression(base, type, isReversed)
    }

    fun asExpressionPart(base: Expression) = parser {
        buffer.get().expect("as").expect(TokenType.IDENTIFIER)
        val isSoft = include(
            parser { buffer.get().expect("?").expect(TokenType.PUNCTUATION) }.optional()
        ).getOrNull() != null
        val type = include(TypeParser())
        return@parser AsExpression(base, type, isSoft)
    }

    fun callExpressionPart(base: Expression): Parser<CallExpression> = parser {
        include(
            parser<CallExpression> {
                val arguments = include(ListParser(ExpressionParser()))
                val closure = include(ClosureExpressionParser())
                ClosureCallExpression(base, arguments, closure)
            } or
                    parser<CallExpression> {
                        val arguments = include(ListParser(ExpressionParser()))
                        CallExpression(base, arguments)
                    } or
                    parser {
                        val closure = include(ClosureExpressionParser())
                        ClosureCallExpression(base, emptyList(), closure)
                    }
        )
    }

    fun memberReferencePart(base: Expression) = parser {
        repeat(2) {
            buffer.get().expect(TokenType.PUNCTUATION).expect(":")
        }
        val member = include(IdentifierParser())
        MemberReferenceExpression(member, base)
    }

    fun memberExpressionPart(base: Expression) = parser {
        val isFuzzy = include(
            parser {
                buffer.get().expect(TokenType.PUNCTUATION).expect("?")
            }.optional()
        ).getOrNull() != null
        buffer.get().expect(TokenType.PUNCTUATION).expect(".")
        val name = include(IdentifierParser())
        return@parser MemberExpression(base, name, isFuzzy = isFuzzy)
    }

    override fun parse(): Expression {
        var base: Expression = include(base())
        while (true) {
            val result = include(part(base).optional())
            if (result.getOrNull() == null) break
            else base = result.getOrNull()!!
        }
        return base
    }

}
