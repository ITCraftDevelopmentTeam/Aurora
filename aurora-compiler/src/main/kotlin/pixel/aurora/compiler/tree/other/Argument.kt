package pixel.aurora.compiler.tree.other

import pixel.aurora.compiler.tree.Expression
import pixel.aurora.compiler.tree.Identifier
import pixel.aurora.compiler.tree.Node

open class Argument(private val value: Expression) : Node {

    override fun getNodeName() = "Argument"

    @Node.Property
    fun getArgumentValue() = value

}

open class NamedArgument(private val name: Identifier, value: Expression) : Argument(value) {

    override fun getNodeName() = "NamedArgument"

    @Node.Property
    fun getParameter() = name

}