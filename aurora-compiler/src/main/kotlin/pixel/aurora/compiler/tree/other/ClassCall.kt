package pixel.aurora.compiler.tree.other

import pixel.aurora.compiler.tree.Node
import pixel.aurora.compiler.tree.SimpleType

class ClassCall(private val type: SimpleType, private val arguments: List<Argument>) : Node {

    override fun getNodeName() = "ClassCall"

    @Node.Property
    fun getType() = type

    @Node.Property
    fun getParameters() = arguments

}