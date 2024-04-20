package pixel.aurora.tokenizer

import org.junit.jupiter.api.Test
import java.nio.CharBuffer
import kotlin.test.assertEquals

class TokenizerTests {

    @Test
    fun `Tokenizer Tests`() {
        val text = """
            identifierName"String\\\\";true;false;null.;.1e+2000;2.1e-222,888888888888888888888,0o77777777777777777777777777777777777777777777777,0b111111111111111111111111111111111111111111111111111111111111111111111, 0xffffffFFFFFffffffFFFFFffffffFFFFFffffffFFFFFffffffFFFFFffffffFFFFFffffffFFFFFffffffFFFFFffffffFFFFF
            [name, name, name]
        """.trimIndent()
        val tokenizer = Tokenizer(CharBuffer.wrap(text))
        val buffer = TokenBuffer(tokenizer)
        assertEquals(buffer.getTokens().size, 32)
    }

}