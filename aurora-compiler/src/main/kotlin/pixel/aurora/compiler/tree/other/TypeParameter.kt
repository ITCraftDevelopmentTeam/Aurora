package pixel.aurora.compiler.tree.other

import pixel.aurora.compiler.tree.Identifier
import pixel.aurora.compiler.tree.Node
import pixel.aurora.compiler.tree.Type

class TypeParameter(private val name: Identifier, private val constraintType: Type?) : Node {

    override fun getNodeName() = "TypeParameter"

    @Node.Property
    fun getName() = name

    @Node.Property
    fun getConstraintType() = constraintType

}