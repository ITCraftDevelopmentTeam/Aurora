package pixel.aurora.compiler.tree

interface Statement : Node {

    override fun getNodeName() = "Statement"

    fun getStatementName(): String

}

class BlockStatement(private val body: List<Statement>) : Statement {

    @Node.Property
    fun getBody() = body

    override fun getStatementName() = "BlockStatement"

}

class ExpressionStatement(private val expression: Expression) : Statement {

    override fun getStatementName() = "ExpressionStatement"

    @Node.Property
    fun getExpression() = expression

}

object EmptyStatement : Statement {

    override fun getStatementName() = "EmptyStatement"

}

class ReturnStatement(private val expression: Expression?) : Statement {

    override fun getStatementName() = "ReturnStatement"

    @Node.Property
    fun getExpression() = expression

}

class InnerDeclarationStatement(private val declaration: Declaration) : Statement {

    override fun getStatementName() = "InnerDeclarationStatement"

    @Node.Property
    fun getDeclaration() = declaration

}

class IfStatement(private val test: Expression, private val consequent: Statement, private val alternate: Statement?) : Statement {

    override fun getStatementName() = "IfStatement"

    @Node.Property
    fun getTest() = test

    @Node.Property
    fun getConsequent() = consequent

    @Node.Property
    fun getAlternate() = alternate

}
