package pixel.aurora.compiler.tree.other

import pixel.aurora.compiler.tree.Node

enum class VisibilityMode : Node {

    PUBLIC, PRIVATE, PROTECTED, INTERNAL;

    companion object {

        fun of(name: String) = entries.first { it.name.lowercase() == name }

    }

    override fun getNodeName() = "VisibleMode"

    @Node.Property
    fun getVisibilityModeName() = this.name.lowercase().replaceFirstChar { it.uppercase() }

}