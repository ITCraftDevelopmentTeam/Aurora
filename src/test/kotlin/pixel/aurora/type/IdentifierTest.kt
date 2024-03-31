package pixel.aurora.type

import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.junit.jupiter.api.Test
import pixel.aurora.Aurora

class IdentifierTest {

    private val jsonMapper = JsonMapper()

    @Test
    fun `Serialization Tests`() {
        val identifierString1 = "\"hello\""
        val identifierString2 = "\"hello:world\""
        val identifier1 = jsonMapper.readValue<Identifier>(identifierString1)
        val identifier2 = jsonMapper.readValue<Identifier>(identifierString2)
        assert(identifier1.getNamespace() == Aurora.namespace && identifier1.getPath() == "hello")
        assert(identifier2.getNamespace() == "hello" && identifier2.getPath() == "world")
        assert(jsonMapper.writeValueAsString(identifier2) == identifierString2)
    }

}