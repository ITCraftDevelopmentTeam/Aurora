package pixel.aurora.tree

interface Statement : Node {

    override fun getNodeName() = "Statement"

    fun getStatementName(): String

}

class ExpressionStatement(private val expression: Expression) : Statement {

    override fun getStatementName() = "ExpressionStatement"
    fun getExpression() = expression

}

class PackageDefinitionStatement(private val location: LocationIdentifierLiteral) : Statement {

    override fun getStatementName() = "PackageDefinitionStatement"

    fun getLocation() = location

}
