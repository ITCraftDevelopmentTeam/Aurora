package pixel.aurora.tree

interface Declaration : Node {

    override fun getNodeName() = "Declaration"
    fun getDeclarationName(): String

}

class CommentsDeclaration(private val text: String, private val isBlock: Boolean = false) : Declaration {

    override fun getDeclarationName() = "CommentsDeclaration"
    fun getComments() = text
    fun isBlock() = isBlock

}

class FunctionDeclaration(private val name: Identifier, private val body: List<Statement>) : Declaration {

    override fun getDeclarationName() = "FunctionDeclaration"
    fun getFunctionName() = name
    fun getBody() = body

}
