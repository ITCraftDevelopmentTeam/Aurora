package pixel.aurora.tree.other

import pixel.aurora.tree.Identifier
import pixel.aurora.tree.Node
import pixel.aurora.tree.Type

class TypeParameter(private val name: Identifier, private val constraintType: Type?) : Node {

    override fun getNodeName() = "TypeParameter"

    @Node.Property
    fun getName() = name

    @Node.Property
    fun getConstraintType() = constraintType

}