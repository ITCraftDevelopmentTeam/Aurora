package pixel.aurora.tree

import java.math.BigDecimal

interface Expression : Node {

    override fun getNodeName() = "Expression"

    fun getExpressionName(): String

}

class Identifier(private val name: String) : Expression {

    override fun getExpressionName() = "Identifier"
    fun getIdentifierName() = name

}

interface Literal<T> : Expression {

    override fun getExpressionName() = "Literal"
    fun getLiteralName(): String
    fun getLiteral(): T

}

open class UnknownLiteral<T>(private val literalName: String = "UnknownLiteral", private val literal: T) : Literal<T> {

    override fun getLiteralName() = literalName
    override fun getLiteral() = literal

}

class StringLiteral(literal: String) : UnknownLiteral<String>("StringLiteral", literal)
class NumberLiteral(literal: BigDecimal) : UnknownLiteral<BigDecimal>("NumberLiteral", literal)
enum class BooleanLiteral(private val literal: Boolean) : Literal<Boolean> {
    TRUE(true), FALSE(false);

    override fun getLiteral() = literal
    override fun getLiteralName() = "BooleanLiteral"
}

object NullLiteral : UnknownLiteral<Any?>("NullLiteral", null)

class MemberExpression(private val expression: Expression, private val member: Identifier) : Expression {

    override fun getExpressionName() = "MemberExpression"

    fun getExpression() = expression
    fun getMember() = member

}

class CallExpression(private val callee: Expression, private val arguments: List<Expression>) : Expression {

    override fun getExpressionName() = "MemberExpression"

    fun getCallee() = callee
    fun getArguments() = arguments

}

class AssignmentExpression(private val left: Expression, private val right: Expression, private val operator: String) : Expression {

    override fun getExpressionName() = "AssignmentExpression"
    fun getLeft() = left
    fun getRight() = right
    fun getOperator() = operator

}

class BinaryExpression(private val left: Expression, private val right: Expression, private val operator: String) : Expression {

    override fun getExpressionName() = "BinaryExpression"

    fun getLeft() = left
    fun getRight() = right
    fun getOperator() = operator

}

class UnaryExpression(private val expression: Expression, private val operator: String) : Expression {

    override fun getExpressionName() = "UnaryExpression"

    fun getExpression() = expression
    fun getOperator() = operator

}

class UpdateExpression(private val expression: Expression, private val operator: String) : Expression {

    override fun getExpressionName() = "UpdateExpression"

    fun getExpression() = expression
    fun getOperator() = operator

}

class AsExpression(private val expression: Expression, private val type: Type, private val isSoft: Boolean) : Expression {

    override fun getExpressionName() = "AsExpression"

    fun getExpression() = expression
    fun getType() = type
    fun isSoft() = isSoft

}

class IsExpression(private val expression: Expression, private val type: Type, private val isReversed: Boolean) : Expression {

    override fun getExpressionName() = "IsExpression"

    fun getExpression() = expression
    fun getType() = type
    fun isReversed() = isReversed

}
