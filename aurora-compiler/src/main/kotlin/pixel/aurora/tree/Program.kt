package pixel.aurora.tree

class Program(
    private val packageDeclaration: PackageDeclaration,
    private val body: List<Declaration>
) : Node {

    class PackageDeclaration(private val name: String) : Declaration {
        override fun getDeclarationName() = "PackageDeclaration"
        fun getPackageName() = name
    }

    @Node.Property
    fun getPackageDeclaration() = packageDeclaration
    override fun getNodeName() = "Program"

    @Node.Property
    fun getBody() = body

}