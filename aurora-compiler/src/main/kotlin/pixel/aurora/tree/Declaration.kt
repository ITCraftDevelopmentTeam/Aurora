package pixel.aurora.tree

import pixel.aurora.tree.other.Parameter

interface Declaration : Node {

    override fun getNodeName() = "Declaration"
    fun getDeclarationName(): String

}

object EmptyDeclaration : Declaration {
    override fun getDeclarationName() = "EmptyDeclaration"
}

interface FunctionDeclaration : Declaration {

    override fun getDeclarationName() = "FunctionDeclaration"
    fun getFunctionDeclarationName(): String

    @Node.Property
    fun getFunctionName(): Identifier

    @Node.Property
    fun getFunctionParameters(): List<Parameter>

    @Node.Property
    fun getFunctionReturnType(): Type

}

class BlockFunctionDeclaration(
    private val name: Identifier,
    private val parameters: List<Parameter>,
    private val returnType: Type,
    private val body: List<Statement>
) : FunctionDeclaration {

    override fun getFunctionDeclarationName() = "BlockFunctionDeclaration"
    override fun getFunctionName() = name
    override fun getFunctionParameters() = parameters
    override fun getFunctionReturnType() = returnType

    @Node.Property
    fun getBody() = body
}

class ExpressionFunctionDeclaration(
    private val name: Identifier,
    private val parameters: List<Parameter>,
    private val returnType: Type,
    private val expression: Expression
) :
    FunctionDeclaration {

    override fun getFunctionDeclarationName() = "ExpressionFunctionDeclaration"

    override fun getFunctionName() = name
    override fun getFunctionParameters() = parameters
    override fun getFunctionReturnType() = returnType

    @Node.Property
    fun getExpression() = expression

}
