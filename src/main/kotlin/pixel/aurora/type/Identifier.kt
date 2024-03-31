package pixel.aurora.type

import pixel.aurora.Aurora
import java.util.*

object Identifiers {

    fun parse(identifier: String, defaultNamespace: String? = null): Optional<Identifier> {
        if (identifier.trim().isEmpty()) return Optional.empty()
        val split = identifier.split(":")
        if (split.isEmpty()) return Optional.empty()
        if (split.size == 1) return Optional.of(Identifier(defaultNamespace ?: Aurora.namespace, split.first()))
        return Optional.of(Identifier(split.first(), split.subList(1, split.size).joinToString(separator = ":")))
    }

    fun parseOrThrow(identifier: String, defaultNamespace: String? = null): Identifier = parse(identifier, defaultNamespace = defaultNamespace).orElseThrow()

    fun isNamespace(namespace: String) = namespace.trim().isNotEmpty() && namespace.chars().allMatch {
        val character = it.toChar()
        character == '<' || character == '>' || character == '_' || character in 'A'..'Z' || character in 'a'..'z' || character in '0'..'9' || character == '-'
    }

    fun isPath(path: String) = path.trim().isNotEmpty() && path.chars().allMatch {
        val character = it.toChar()
        character == '.' || character == '_' || character in 'A'..'Z' || character in 'a'..'z' || character in '0'..'9' || character == '/' || character == '-'
    }

}

class Identifier(private val namespace: String, private val path: String) {

    fun getNamespace() = namespace
    fun getPath() = path

    init {
        assert(Identifiers.isNamespace(namespace)) { "Invalid namespace: $namespace" }
        assert(Identifiers.isPath(path)) { "Invalid path: $path" }
    }

    override fun hashCode() = toString().hashCode()
    override fun toString() = "${getNamespace()}:${getPath()}"
    override fun equals(other: Any?) = other === this || (other != null && other.toString() == toString())

}

fun identifierOf(identifier: String, defaultNamespace: String? = null) = Identifiers.parseOrThrow(identifier, defaultNamespace = defaultNamespace)
