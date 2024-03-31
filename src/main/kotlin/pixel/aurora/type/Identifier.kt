package pixel.aurora.type

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import pixel.aurora.Aurora
import java.util.*

object Identifiers {

    fun parse(identifier: String): Optional<Identifier> {
        if (identifier.trim().isEmpty()) return Optional.empty()
        val split = identifier.split(":")
        if (split.isEmpty()) return Optional.empty()
        if (split.size == 1) return Optional.of(Identifier(Aurora.namespace, split.first()))
        return Optional.of(Identifier(split.first(), split.subList(1, split.size).joinToString(separator = ":")))
    }

    fun parseOrThrow(identifier: String): Identifier = parse(identifier).orElseThrow()

    fun isNamespace(namespace: String) = namespace.trim().isNotEmpty() && namespace.chars().allMatch {
        val character = it.toChar()
        character == '_' || character in 'A'..'Z' || character in 'a'..'z' || character in '0'..'9' || character == '-'
    }

    fun isPath(path: String) = path.trim().isNotEmpty() && path.chars().allMatch {
        val character = it.toChar()
        character == '.' || character == '_' || character in 'A'..'Z' || character in 'a'..'z' || character in '0'..'9' || character == '/' || character == '-'
    }

}

@JsonSerialize(using = IdentifierSerializer::class)
@JsonDeserialize(using = IdentifierDeserializer::class)
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

class IdentifierDeserializer : StdDeserializer<Identifier>(Identifier::class.java) {

    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): Identifier {
        return Identifiers.parseOrThrow(p.valueAsString)
    }

}
class IdentifierSerializer : StdSerializer<Identifier>(Identifier::class.java) {

    override fun serialize(value: Identifier, gen: JsonGenerator, serializers: SerializerProvider) {
        gen.writeString(value.toString())
    }

}

