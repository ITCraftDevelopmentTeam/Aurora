package pixel.aurora.tree

interface Type : Node {

    override fun getNodeName() = "Type"

    fun getTypeName(): String

}

open class SimpleType(
    private val name: String,
    private val typeArguments: List<Type> = emptyList(),
    private val nullable: Boolean = false
) : Type {

    object None : SimpleType("None")

    override fun getTypeName() = "SimpleType"

    @Node.Property
    fun getType() = name

    @Node.Property
    fun getTypeParameters() = typeArguments

    @Node.Property
    fun isNullable() = nullable

}
