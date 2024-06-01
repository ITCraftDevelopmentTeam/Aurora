package pixel.aurora.tree

import pixel.aurora.tree.other.Parameter
import pixel.aurora.tree.other.TypeParameter
import pixel.aurora.tree.other.VisibilityMode

interface Declaration : Node {

    override fun getNodeName() = "Declaration"
    fun getDeclarationName(): String

}

object EmptyDeclaration : Declaration {
    override fun getDeclarationName() = "EmptyDeclaration"
}

abstract class FunctionDeclaration(
    private val name: Identifier,
    private val typeParameters: List<TypeParameter>,
    private val parameters: List<Parameter>,
    private val returnType: Type,
    private val visibilityMode: VisibilityMode = VisibilityMode.PUBLIC
) : Declaration {

    override fun getDeclarationName() = "FunctionDeclaration"
    abstract fun getFunctionDeclarationName(): String

    @Node.Property
    fun getFunctionName(): Identifier = name

    @Node.Property
    fun getFunctionTypeParameters() = typeParameters

    @Node.Property
    fun getFunctionParameters(): List<Parameter> = parameters

    @Node.Property
    fun getFunctionReturnType(): Type = returnType

    @Node.Property
    fun getFunctionVisibilityMode(): VisibilityMode = visibilityMode

}

class BlockFunctionDeclaration(
    name: Identifier,
    typeParameters: List<TypeParameter>,
    parameters: List<Parameter>,
    returnType: Type,
    private val body: List<Statement>,
    visibilityMode: VisibilityMode = VisibilityMode.PUBLIC
) : FunctionDeclaration(name, typeParameters, parameters, returnType, visibilityMode) {

    override fun getFunctionDeclarationName() = "BlockFunctionDeclaration"

    @Node.Property
    fun getBody() = body

}

class ExpressionFunctionDeclaration(
    name: Identifier,
    typeParameters: List<TypeParameter>,
    parameters: List<Parameter>,
    returnType: Type,
    private val expression: Expression,
    visibilityMode: VisibilityMode = VisibilityMode.PUBLIC
) :
    FunctionDeclaration(name, typeParameters, parameters, returnType, visibilityMode) {

    override fun getFunctionDeclarationName() = "ExpressionFunctionDeclaration"

    @Node.Property
    fun getExpression() = expression

}

class VariableDeclaration(
    private val kind: Kind,
    private val name: Identifier,
    private val type: Type? = null,
    private val init: Expression? = null,
    private val visibilityMode: VisibilityMode = VisibilityMode.INTERNAL
) : Declaration {

    override fun getDeclarationName() = "VariableDeclaration"

    enum class Kind {
        CONST, LET;
    }

    @Node.Property
    fun getVariableKind() = kind

    @Node.Property
    fun getVariableName() = name

    @Node.Property
    fun getVariableType() = type

    @Node.Property
    fun getVariableValue() = init

    @Node.Property
    fun getVariableVisibilityMode() = visibilityMode

}
