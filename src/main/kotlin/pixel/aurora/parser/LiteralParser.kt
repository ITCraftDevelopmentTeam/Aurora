package pixel.aurora.parser

import pixel.aurora.tree.LocationIdentifierLiteral
import pixel.aurora.type.LocationIdentifiers
import pixel.aurora.type.locationIdentifierOf
import java.nio.BufferUnderflowException

class LocationIdentifierLiteralParser : Parser<LocationIdentifierLiteral>() {

    override fun parse(): LocationIdentifierLiteral {
        val buffer = getBuffer()
        if (buffer.get() != '@') throw makeError("Expect '@'")
        if (buffer.get() != '[') throw makeError("Expect '['")
        var literal = ""
        var current: Char?
        try {
            while (buffer.get() != ']') {
                current = buffer.get(buffer.position() - 1)
                if (current != ':' && !(LocationIdentifiers.isNamespace(current.toString()) || LocationIdentifiers.isPath(current.toString())))
                    throw makeError("Invalid location identifier character: $current")
                if (current == ':' && ':' in literal)
                    throw makeError("Duplicate ':'")
                literal += current
            }
            try {
                return LocationIdentifierLiteral(locationIdentifierOf(literal, defaultNamespace = "<anonymous>"))
            } catch (assertionError: AssertionError) {
                throw makeError("Invalid location identifier: $literal")
            }
        } catch (exception: BufferUnderflowException) {
            throw makeError("Expect ']'")
        }
    }

}