package pixel.aurora.tree

import pixel.aurora.type.LocationIdentifier
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
class LocationIdentifierLiteral(literal: LocationIdentifier) :
    UnknownLiteral<LocationIdentifier>("LocationIdentifierLiteral", literal)
