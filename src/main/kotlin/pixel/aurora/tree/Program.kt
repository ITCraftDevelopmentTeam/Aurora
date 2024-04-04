package pixel.aurora.tree

class Program(private val packageDefinitionStatement: PackageDefinitionStatement, private val body: List<Declaration>) : Node {

    override fun getNodeName() = "Program"

    fun getPackageDefinition() = packageDefinitionStatement
    fun getBody() = body

}