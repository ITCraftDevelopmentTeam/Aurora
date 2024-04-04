package pixel.aurora.type

import pixel.aurora.Aurora
import java.util.*

object LocationIdentifiers {

    fun parse(identifier: String, defaultNamespace: String? = null): Optional<LocationIdentifier> {
        if (identifier.trim().isEmpty()) return Optional.empty()
        val split = identifier.split(":")
        if (split.isEmpty()) return Optional.empty()
        if (split.size == 1) return Optional.of(LocationIdentifier(defaultNamespace ?: Aurora.namespace, split.first()))
        return Optional.of(LocationIdentifier(split.first(), split.subList(1, split.size).joinToString(separator = ":")))
    }

    fun parseOrThrow(identifier: String, defaultNamespace: String? = null): LocationIdentifier = parse(identifier, defaultNamespace = defaultNamespace).orElseThrow()

    fun isNamespace(namespace: String) = namespace.trim().isNotEmpty() && namespace.chars().allMatch {
        val character = it.toChar()
        character == '<' || character == '>' || character == '_' || character in 'A'..'Z' || character in 'a'..'z' || character in '0'..'9' || character == '-'
    }

    fun isPath(path: String) = path.trim().isNotEmpty() && path.chars().allMatch {
        val character = it.toChar()
        character == ':' || character == '.' || character == '_' || character in 'A'..'Z' || character in 'a'..'z' || character in '0'..'9' || character == '/' || character == '-'
    }

}

class LocationIdentifier(private val namespace: String, private val path: String) {

    fun getNamespace() = namespace
    fun getPath() = path

    init {
        assert(LocationIdentifiers.isNamespace(namespace)) { "Invalid namespace: $namespace" }
        assert(LocationIdentifiers.isPath(path)) { "Invalid path: $path" }
    }

    override fun hashCode() = toString().hashCode()
    override fun toString() = "${getNamespace()}:${getPath()}"
    override fun equals(other: Any?) = other === this || (other != null && other.toString() == toString())

}

fun locationIdentifierOf(identifier: String, defaultNamespace: String? = null) = LocationIdentifiers.parseOrThrow(identifier, defaultNamespace = defaultNamespace)
