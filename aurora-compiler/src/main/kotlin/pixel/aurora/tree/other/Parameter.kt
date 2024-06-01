package pixel.aurora.tree.other

import pixel.aurora.tree.Identifier
import pixel.aurora.tree.Node
import pixel.aurora.tree.Type

class Parameter(private val name: Identifier, private val type: Type) : Node {

    override fun getNodeName() = "Parameter"

    fun getName() = name
    fun getType() = type

}
