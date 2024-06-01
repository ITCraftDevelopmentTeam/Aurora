package pixel.aurora.tree

import pixel.aurora.tree.other.Parameter
import java.math.BigDecimal

interface Expression : Node {

    override fun getNodeName() = "Expression"

    fun getExpressionName(): String

}

class Identifier(private val name: String) : Expression {

    override fun getExpressionName() = "Identifier"

    @Node.Property
    fun getIdentifierName() = name

}

interface Literal<T> : Expression {

    override fun getExpressionName() = "Literal"
    fun getLiteralName(): String
    fun getLiteral(): T

}

open class UnknownLiteral<T>(private val literalName: String = "UnknownLiteral", private val literal: T) : Literal<T> {

    override fun getLiteralName() = literalName

    @Node.Property
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

    @Node.Property
    fun getExpression() = expression

    @Node.Property
    fun getMember() = member

}

open class CallExpression(private val callee: Expression, private val arguments: List<Expression>) : Expression {

    override fun getExpressionName() = "MemberExpression"

    @Node.Property
    fun getCallee() = callee

    @Node.Property
    fun getArguments() = arguments

}

open class ClosureCallExpression(callee: Expression, arguments: List<Expression>, private val closureExpression: ClosureExpression) : CallExpression(callee, arguments) {

    @Node.Property
    fun getClosure() = closureExpression

}

class AssignmentExpression(private val left: Expression, private val right: Expression, private val operator: String) : Expression {

    override fun getExpressionName() = "AssignmentExpression"

    @Node.Property
    fun getLeft() = left

    @Node.Property
    fun getRight() = right

    @Node.Property
    fun getOperator() = operator

}

class BinaryExpression(private val left: Expression, private val right: Expression, private val operator: String) : Expression {

    override fun getExpressionName() = "BinaryExpression"

    @Node.Property
    fun getLeft() = left

    @Node.Property
    fun getRight() = right

    @Node.Property
    fun getOperator() = operator

}

class UnaryExpression(private val expression: Expression, private val operator: String) : Expression {

    override fun getExpressionName() = "UnaryExpression"

    @Node.Property
    fun getExpression() = expression

    @Node.Property
    fun getOperator() = operator

}

class UpdateExpression(private val expression: Expression, private val operator: String) : Expression {

    override fun getExpressionName() = "UpdateExpression"

    @Node.Property
    fun getExpression() = expression

    @Node.Property
    fun getOperator() = operator

}

class AsExpression(private val expression: Expression, private val type: Type, private val isSoft: Boolean) : Expression {

    override fun getExpressionName() = "AsExpression"

    @Node.Property
    fun getExpression() = expression

    @Node.Property
    fun getType() = type

    @Node.Property
    fun isSoft() = isSoft

}

class IsExpression(private val expression: Expression, private val type: Type, private val isReversed: Boolean) : Expression {

    override fun getExpressionName() = "IsExpression"

    @Node.Property
    fun getExpression() = expression

    @Node.Property
    fun getType() = type

    @Node.Property
    fun isReversed() = isReversed

}

class ClosureExpression(private val parameters: List<Parameter>, private val body: List<Statement>) : Expression {

    @Node.Property
    fun getParameters() = parameters

    @Node.Property
    fun getBody() = body

    override fun getExpressionName() = "ClosureExpression"

}
