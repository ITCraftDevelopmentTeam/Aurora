package pixel.aurora.tree

interface Statement : Node {

    override fun getNodeName() = "Statement"

    fun getStatementName(): String

}

class ExpressionStatement(private val expression: Expression) : Statement {

    override fun getStatementName() = "ExpressionStatement"
    fun getExpression() = expression

}
