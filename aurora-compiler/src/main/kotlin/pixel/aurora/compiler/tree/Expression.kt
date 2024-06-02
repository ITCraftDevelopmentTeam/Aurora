package pixel.aurora.compiler.tree

import pixel.aurora.compiler.tree.other.AnnotationUsing
import pixel.aurora.compiler.tree.other.Argument
import pixel.aurora.compiler.tree.other.ClassCall
import pixel.aurora.compiler.tree.other.Parameter
import java.math.BigDecimal

interface Expression : Node {

    override fun getNodeName() = "Expression"

    fun getExpressionName(): String

}

open class Identifier(private val name: String) : Expression {

    override fun getExpressionName() = "Identifier"

    @Node.Property
    fun getIdentifierName() = name

}

class ThisExpression(private val label: Identifier? = null) : Identifier("this") {

    override fun getExpressionName() = "ThisExpression"

    @Node.Property
    fun getLabel() = label

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
open class BooleanLiteral(private val literal: Boolean) : Literal<Boolean> {

    class True : BooleanLiteral(true)
    class False : BooleanLiteral(false)

    override fun getLiteral() = literal
    override fun getLiteralName() = "BooleanLiteral"
}

class NullLiteral : UnknownLiteral<Any?>("NullLiteral", null)

class MemberExpression(
    private val expression: Expression,
    private val member: Identifier,
    private val isFuzzy: Boolean = false
) : Expression {

    override fun getExpressionName() = "MemberExpression"

    @Node.Property
    fun isFuzzy() = isFuzzy

    @Node.Property
    fun getExpression() = expression

    @Node.Property
    fun getMember() = member

}

open class CallExpression(
    private val callee: Expression,
    private val arguments: List<Argument>,
    private val typeArguments: List<Type> = emptyList()
) :
    Expression {

    override fun getExpressionName() = "MemberExpression"

    @Node.Property
    fun getCallee() = callee

    @Node.Property
    fun getArguments() = arguments

    @Node.Property
    fun getTypeArguments() = typeArguments

}

open class ClosureCallExpression(
    callee: Expression,
    arguments: List<Argument>,
    private val lambdaExpression: LambdaExpression,
    typeArguments: List<Type> = emptyList()
) : CallExpression(callee, arguments, typeArguments) {

    @Node.Property
    fun getClosure() = lambdaExpression

}

class AssignmentExpression(private val left: Expression, private val right: Expression, private val operator: String) :
    Expression {

    override fun getExpressionName() = "AssignmentExpression"

    @Node.Property
    fun getLeft() = left

    @Node.Property
    fun getRight() = right

    @Node.Property
    fun getOperator() = operator

}

class BinaryExpression(private val left: Expression, private val right: Expression, private val operator: String) :
    Expression {

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

class AsExpression(private val expression: Expression, private val type: Type, private val isSoft: Boolean) :
    Expression {

    override fun getExpressionName() = "AsExpression"

    @Node.Property
    fun getExpression() = expression

    @Node.Property
    fun getType() = type

    @Node.Property
    fun isSoft() = isSoft

}

class IsExpression(private val expression: Expression, private val type: Type, private val isReversed: Boolean) :
    Expression {

    override fun getExpressionName() = "IsExpression"

    @Node.Property
    fun getExpression() = expression

    @Node.Property
    fun getType() = type

    @Node.Property
    fun isReversed() = isReversed

}

class LambdaExpression(
    private val parameters: List<Parameter>,
    private val body: List<Statement>,
    private val annotations: List<AnnotationUsing>
) :
    Expression {

    @Node.Property
    fun getParameters() = parameters

    @Node.Property
    fun getBody() = body

    @Node.Property
    fun getAnnotations() = annotations

    override fun getExpressionName() = "ClosureExpression"

}

class DistinctCastingExpression(private val expression: Expression) : Expression {

    override fun getExpressionName() = "DistinctCastingExpression"

    @Node.Property
    fun getExpression() = expression

}

class MemberReferenceExpression(private val member: Identifier, private val expression: Expression? = null) :
    Expression {

    @Node.Property
    fun getMember() = member

    @Node.Property
    fun getExpression() = expression

    override fun getExpressionName() = "MemberReferenceExpression"

}

class ObjectExpression(
    private val implements: List<SimpleType>,
    private val extends: ClassCall,
    private val body: List<Declaration>,
    private val annotations: List<AnnotationUsing>
) : Expression {

    override fun getExpressionName() = "ObjectExpression"

    @Node.Property
    fun getObjectImplements() = implements

    @Node.Property
    fun getObjectExtends() = extends

    @Node.Property
    fun getObjectBody() = body

    @Node.Property
    fun getObjectAnnotations() = annotations

}
