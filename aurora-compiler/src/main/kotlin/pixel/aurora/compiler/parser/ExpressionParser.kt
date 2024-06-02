package pixel.aurora.compiler.parser

import pixel.aurora.compiler.parser.expression.*
import pixel.aurora.compiler.parser.other.ArgumentParser
import pixel.aurora.compiler.parser.other.NamedArgumentParser
import pixel.aurora.compiler.parser.util.ListParser
import pixel.aurora.compiler.parser.util.StringUtils.chunkPunctuationAndIdentifier
import pixel.aurora.compiler.tree.*

class BinaryExpressionParser(val left: Expression) : Parser<BinaryExpression>() {

    fun operator(operator: String) = parser {
        for (character in operator) buffer.get().expectPunctuation(character)
        return@parser BinaryExpression(left, include(ExpressionParser()), operator)
    }

    fun keyword(operator: String) = parser {
        for (chunk in chunkPunctuationAndIdentifier(operator)) buffer.get().expect(chunk.first).expect(chunk.second)
        return@parser BinaryExpression(left, include(ExpressionParser()), operator)
    }

    override fun parse(): BinaryExpression {
        return include(
            operator("+") or operator("-") or
                    operator("*") or operator("/") or
                    operator("%") or
                    operator("===") or operator("==") or
                    operator("!==") or operator("!=") or
                    operator("..") or operator(".<") or
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
            buffer.get().expectPunctuation(character)
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
            buffer.get().expectPunctuation(character)
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
        LiteralExpressionParser() or IndependentExpressionParser() or
                UnaryExpressionParser() or LambdaExpressionParser() or MemberReferenceExpressionParser() or
                ObjectExpressionParser() or IdentifierParser()

    fun part(base: Expression) = parser {
        include(
            memberReferencePart(base) or distinctCastingPart(base) or memberExpressionPart(base) or callExpressionPart(
                base
            ) or AssignmentExpressionParser(base) or BinaryExpressionParser(base) or UpdateExpressionParser(base) or asExpressionPart(
                base
            ) or isExpressionPart(base) or operatorGetPart(base)
        )
    }

    fun operatorGetPart(base: Expression) = parser {
        val index = include(ListParser(NamedArgumentParser() or ArgumentParser(), "[", "]"))
        CallExpression(MemberExpression(base, Identifier("get")), index)
    }

    fun distinctCastingPart(base: Expression) = parser {
        repeat(2) {
            buffer.get().expectPunctuation('!')
        }
        DistinctCastingExpression(base)
    }

    fun isExpressionPart(base: Expression) = parser {
        val isReversed = include(
            parser { buffer.get().expectPunctuation('!') }.optional()
        ).getOrNull() != null
        buffer.get().expectIdentifier("is")
        val type = include(TypeParser())
        return@parser IsExpression(base, type, isReversed)
    }

    fun asExpressionPart(base: Expression) = parser {
        buffer.get().expectIdentifier("as")
        val isSoft = include(
            parser { buffer.get().expectPunctuation('?') }.optional()
        ).getOrNull() != null
        val type = include(TypeParser())
        return@parser AsExpression(base, type, isSoft)
    }

    fun callExpressionPart(base: Expression): Parser<CallExpression> = parser {
        val typeArguments = include(ListParser(TypeParser(), "<", ">").optional()).getOrNull() ?: emptyList()
        include(
            parser<CallExpression> {
                val arguments = include(ListParser(NamedArgumentParser() or ArgumentParser()))
                val closure = include(LambdaExpressionParser())
                ClosureCallExpression(base, arguments, closure, typeArguments = typeArguments)
            } or parser<CallExpression> {
                val arguments = include(ListParser(NamedArgumentParser() or ArgumentParser()))
                CallExpression(base, arguments, typeArguments = typeArguments)
            } or parser {
                val closure = include(LambdaExpressionParser())
                ClosureCallExpression(base, emptyList(), closure, typeArguments = typeArguments)
            }
        )
    }

    fun memberReferencePart(base: Expression) = parser {
        repeat(2) {
            buffer.get().expectPunctuation(':')
        }
        val member = include(IdentifierParser())
        MemberReferenceExpression(member, base)
    }

    fun memberExpressionPart(base: Expression) = parser {
        val isFuzzy = include(
            parser {
                buffer.get().expectPunctuation('?')
            }.optional()
        ).getOrNull() != null
        buffer.get().expectPunctuation('.')
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
