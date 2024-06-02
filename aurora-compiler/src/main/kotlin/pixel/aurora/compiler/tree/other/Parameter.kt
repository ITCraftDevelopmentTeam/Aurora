package pixel.aurora.compiler.tree.other

import pixel.aurora.compiler.tree.Expression
import pixel.aurora.compiler.tree.Identifier
import pixel.aurora.compiler.tree.Node
import pixel.aurora.compiler.tree.Type

class Parameter(private val name: Identifier, private val type: Type, private val default: Expression? = null) : Node {

    override fun getNodeName() = "Parameter"

    @Node.Property
    fun getName() = name

    @Node.Property
    fun getType() = type

    @Node.Property
    fun getDefault() = default

}
