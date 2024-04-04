package pixel.aurora.parser

import pixel.aurora.tree.Identifier
import pixel.aurora.tree.LocationIdentifierLiteral
import pixel.aurora.tree.PackageDefinitionStatement
import pixel.aurora.type.LocationIdentifiers
import pixel.aurora.type.locationIdentifierOf
import java.nio.BufferUnderflowException

class LocationIdentifierLiteralParser : Parser<LocationIdentifierLiteral>() {

    override fun parse(): LocationIdentifierLiteral {
        val buffer = getBuffer()
        if (buffer.get() != '@') throw makeError(ParserMessages.expect("@"))
        if (buffer.get() != '[') throw makeError(ParserMessages.expect("["))
        try {
            val literal = include(
                characters {
                    LocationIdentifiers.isNamespace(it.toString()) || LocationIdentifiers.isPath(it.toString())
                }
            ).let { String(it) }
            if (buffer.get() != ']') throw makeError(ParserMessages.expect("]"))
            try {
                return LocationIdentifierLiteral(locationIdentifierOf(literal, defaultNamespace = "<anonymous>"))
            } catch (assertionError: AssertionError) {
                throw makeError("Invalid location identifier: $literal")
            }
        } catch (exception: BufferUnderflowException) {
            throw makeError(ParserMessages.expect("]"))
        }
    }

}

class PackageDefinitionStatementParser : Parser<PackageDefinitionStatement>() {

    override fun parse(): PackageDefinitionStatement {
        include(keyword("package"))
        include(whitespace())
        val location = include(LocationIdentifierLiteralParser())
        include(whitespace())
        include(semicolon())
        return PackageDefinitionStatement(location)
    }

}

class IdentifierParser : Parser<Identifier>() {

    override fun parse(): Identifier {
        val first = include(characters(max = 1, min = 1) { it.isJavaIdentifierStart() })
        val text = include(characters(min = 0) { it.isJavaIdentifierPart() })
        return Identifier(String(first) + String(text))
    }

}
