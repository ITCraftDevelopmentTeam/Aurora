package pixel.aurora.compiler.tree

import pixel.aurora.compiler.tree.other.AnnotationUsing

class Program(
    private val packageDeclaration: PackageDeclaration,
    private val importDeclarations: List<ImportDeclaration>,
    private val body: List<Declaration>,
    private val annotations: List<AnnotationUsing>
) : Node {

    class PackageDeclaration(private val name: String) : Declaration {
        override fun getDeclarationName() = "PackageDeclaration"

        @Node.Property
        fun getPackageName() = name
    }

    class ImportDeclaration(private val name: String, private val importAll: Boolean = false) : Declaration {
        override fun getDeclarationName() = "ImportDeclaration"

        @Node.Property
        fun isImportAll() = importAll

        @Node.Property
        fun getPackageName() = name
    }

    @Node.Property
    fun getAnnotations() = annotations

    @Node.Property
    fun getPackageDeclaration() = packageDeclaration

    @Node.Property
    fun getImportDeclarations() = importDeclarations

    override fun getNodeName() = "Program"

    @Node.Property
    fun getBody() = body

}