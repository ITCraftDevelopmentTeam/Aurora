package pixel.aurora.tree

class Program(
    private val packageDefinitionStatement: PackageDefinitionStatement? = null,
    private val body: List<Declaration>
) : Node {

    override fun getNodeName() = "Program"

    fun getPackageDefinition() = packageDefinitionStatement
    fun getBody() = body

}