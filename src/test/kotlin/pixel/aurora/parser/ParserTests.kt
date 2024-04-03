package pixel.aurora.parser

import org.junit.jupiter.api.Test
import pixel.aurora.type.locationIdentifierOf
import java.nio.CharBuffer
import kotlin.test.assertEquals

class ParserTests {

    @Test
    fun `LocationIdentifierLiteral Tests`() {
        val text = """
            @[namespace-<test>:path/to]
        """.trimIndent()
        val parser = LocationIdentifierLiteralParser()
        parser.setBuffer(CharBuffer.wrap(text))
        assertEquals(locationIdentifierOf("namespace-<test>:path/to"), parser.parse().getLiteral())
    }

}