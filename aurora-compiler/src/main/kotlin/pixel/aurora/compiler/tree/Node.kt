package pixel.aurora.compiler.tree

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import kotlin.reflect.KParameter
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberFunctions
import kotlin.reflect.jvm.jvmName

@JsonSerialize(using = NodeSerializer::class)
interface Node {

    @MustBeDocumented
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Property(val name: String = "")

    fun getNodeName(): String

}

class NodeSerializer : JsonSerializer<Node>() {

    override fun serialize(node: Node, generator: JsonGenerator, provider: SerializerProvider) {
        val type = node::class
        val result = mutableMapOf<String, Any?>()
        result += "nodeType" to type.jvmName
        for (member in type.memberFunctions) {
            val annotation = member.findAnnotation<Node.Property>() ?: continue
            if (member.parameters.filterNot(KParameter::isOptional).size > 1) continue
            val name = annotation.name.takeIf(String::isNotEmpty)
                ?: if (member.name.startsWith("get") && member.name.length > 3) {
                    member.name.removePrefix("get").replaceFirstChar { it.lowercase() }
                } else if (member.name.startsWith("is") && member.name.length > 2) {
                    member.name.removePrefix("is").replaceFirstChar { it.lowercase() }
                } else continue
            val self = member.parameters.first { it.name == null }
            val value = member.callBy(mapOf(self to node))
            result += name to value
        }
        if (result.isNotEmpty()) generator.writeObject(result)
        else generator.writeNull()
    }

}
