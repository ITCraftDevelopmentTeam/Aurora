package pixel.aurora.parser

import org.junit.jupiter.api.Test
import pixel.aurora.tree.BlockFunctionDeclaration
import pixel.aurora.tree.CommentsDeclaration
import pixel.aurora.tree.ExpressionFunctionDeclaration
import pixel.aurora.type.locationIdentifierOf
import java.nio.CharBuffer
import kotlin.test.assertEquals
import kotlin.test.assertIs

class ParserTests {

    @Test
    fun `LocationIdentifierLiteral Tests`() {
        val text = """
            @[<-namespace->:root:path/to]
        """.trimIndent()
        val parser = LocationIdentifierLiteralParser()
        parser.setBuffer(CharBuffer.wrap(text))
        assertEquals(locationIdentifierOf("<-namespace->:root:path/to"), parser.parse().getLiteral())
    }

    @Test
    fun `Program Tests`() {
        val text = """
            package@[hello:world];
            /*
               1HelloWorld2 3
            */
            fun abcFunction () {
                @[abc:def];
                @[ghi:jkl];
            }
            fun defFunction () = @[ddd];
        """.trimIndent()
        val parser = ProgramParser()
        parser.setBuffer(CharBuffer.wrap(text))
        val result = parser.parse()
        val body = result.getBody()
        assertIs<CommentsDeclaration>(body[0])
        assertIs<BlockFunctionDeclaration>(body[1])
        assertIs<ExpressionFunctionDeclaration>(body[2])
    }

}