package pixel.aurora.parser

import pixel.aurora.parser.type.SimpleTypeParser
import pixel.aurora.tree.Type

class TypeParser : Parser<Type>() {

    override fun parse(): Type {
        return include(choose(SimpleTypeParser()))
    }

}