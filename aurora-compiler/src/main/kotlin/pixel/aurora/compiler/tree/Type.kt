package pixel.aurora.compiler.tree

import pixel.aurora.compiler.tree.other.Parameter

interface Type : Node {

    override fun getNodeName() = "Type"

    fun getTypeName(): String

}

open class NullableType(private val type: Type) : Type {

    override fun getTypeName() = "NullableType"

    @Node.Property
    fun getType() = type

}

open class SimpleType(
    private val name: String,
    private val typeArguments: List<Type> = emptyList()
) : Type {

    object None : SimpleType("None")
    object Any : SimpleType("Any")

    object Base {
        object Number : SimpleType("Number")
    }

    object Interface {
        object Tuple : SimpleType("Tuple")
    }

    object Annotation {
        object Override : SimpleType("Override")
    }

    override fun getTypeName() = "SimpleType"

    @Node.Property
    fun getType() = name

    @Node.Property
    fun getTypeParameters() = typeArguments

}

open class FunctionType(
    private val parameters: List<Parameter>,
    private val returnType: Type,
    private val thisType: Type? = null
) : Type {

    override fun getTypeName() = "FunctionType"

    @Node.Property
    fun getFunctionParameters() = parameters

    @Node.Property
    fun getFunctionReturnType() = returnType

    @Node.Property
    fun getFunctionThisType() = thisType

}
