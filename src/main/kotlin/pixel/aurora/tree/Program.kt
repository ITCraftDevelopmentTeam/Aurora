package pixel.aurora.tree

class Program(
    private val body: List<Declaration>
) : Node {

    override fun getNodeName() = "Program"

    fun getBody() = body

}