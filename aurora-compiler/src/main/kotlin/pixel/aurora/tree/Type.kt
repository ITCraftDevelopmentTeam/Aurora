package pixel.aurora.tree

interface Type : Node {

    override fun getNodeName() = "Type"

    fun getTypeName(): String

}

open class SimpleType(
    private val name: String,
    private val typeParameters: List<Type> = emptyList(),
    private val nullable: Boolean = false
) : Type {

    object None : SimpleType("None")

    override fun getTypeName() = "SimpleType"
    override fun getNodeName() = name

    fun getTypeParameters() = typeParameters
    fun isNullable() = nullable

}
