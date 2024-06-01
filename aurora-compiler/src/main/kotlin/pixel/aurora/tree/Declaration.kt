package pixel.aurora.tree

interface Declaration : Node {

    override fun getNodeName() = "Declaration"
    fun getDeclarationName(): String

}

object EmptyDeclaration : Declaration {
    override fun getDeclarationName() = "EmptyDeclaration"
}

class CommentsDeclaration(private val text: String, private val isBlock: Boolean = false) : Declaration {

    override fun getDeclarationName() = "CommentsDeclaration"
    fun getComments() = text
    fun isBlock() = isBlock

}

interface FunctionDeclaration : Declaration {

    override fun getDeclarationName() = "FunctionDeclaration"
    fun getFunctionDeclarationName(): String
    fun getFunctionName(): Identifier


}

class BlockFunctionDeclaration(private val name: Identifier, private val body: List<Statement>) : FunctionDeclaration {

    override fun getFunctionDeclarationName() = "BlockFunctionDeclaration"
    override fun getFunctionName() = name
    fun getBody() = body

}

class ExpressionFunctionDeclaration(private val name: Identifier, private val expression: Expression) :
    FunctionDeclaration {

    override fun getFunctionDeclarationName() = "ExpressionFunctionDeclaration"
    override fun getFunctionName() = name
    fun getExpression() = expression

}
