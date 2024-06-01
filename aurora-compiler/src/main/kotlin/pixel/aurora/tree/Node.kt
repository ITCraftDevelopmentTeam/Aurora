package pixel.aurora.tree

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import kotlinx.coroutines.runBlocking
import java.util.*
import kotlin.reflect.KParameter
import kotlin.reflect.full.callSuspendBy
import kotlin.reflect.full.memberFunctions
import kotlin.reflect.jvm.jvmName

@JsonSerialize(using = NodeSerializer::class)
interface Node {

    fun getNodeName(): String

}

class NodeSerializer : JsonSerializer<Node>() {

    override fun serialize(node: Node, generator: JsonGenerator, provider: SerializerProvider) {
        val type = node::class
        val result = mutableMapOf<String, Any?>()
        result += "class" to type.jvmName
        for (member in type.memberFunctions) {
            if (!member.name.startsWith("get") || member.name.length <= 3) continue
            if (member.parameters.filterNot(KParameter::isOptional).size > 1) continue
            val name = member.name.removePrefix("get").replaceFirstChar { it.lowercase(Locale.ROOT) }
            val self = member.parameters.first { it.name == null }
            val value = runBlocking {
                member.callSuspendBy(mapOf(self to node))
            }
            result += name to value
        }
        generator.writeObject(result)
    }

}
