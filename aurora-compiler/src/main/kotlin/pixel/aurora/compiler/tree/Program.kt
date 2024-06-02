package pixel.aurora.compiler.tree

import pixel.aurora.compiler.tree.other.AnnotationUsing

class Program(
    private val packageDeclaration: PackageDeclaration,
    private val body: List<Declaration>,
    private val annotations: List<AnnotationUsing>
) : Node {

    class PackageDeclaration(private val name: String) : Declaration {
        override fun getDeclarationName() = "PackageDeclaration"

        @Node.Property
        fun getPackageName() = name
    }

    @Node.Property
    fun getAnnotations() = annotations

    @Node.Property
    fun getPackageDeclaration() = packageDeclaration
    override fun getNodeName() = "Program"

    @Node.Property
    fun getBody() = body

}