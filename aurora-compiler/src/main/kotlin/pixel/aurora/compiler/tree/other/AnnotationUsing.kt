package pixel.aurora.compiler.tree.other

import pixel.aurora.compiler.tree.Node
import pixel.aurora.compiler.tree.SimpleType

class AnnotationUsing(
    private val annotation: SimpleType,
    private val target: Target? = null,
    private val arguments: List<Argument> = emptyList()
) :
    Node {

    enum class Target(val symbol: String) {
        ANY("any"),
        FILE("file"), PROPERTY("property"),
        FIELD("field"),
        GETTER("getter"), SETTER("setter"),
        PARAMETER("parameter"), SETTER_PARAMETER("setterParameter")
    }

    override fun getNodeName() = "AnnotationUsing"

    @Node.Property
    fun getTarget() = target

    @Node.Property
    fun getAnnotation() = annotation

    @Node.Property
    fun getArguments() = arguments

}