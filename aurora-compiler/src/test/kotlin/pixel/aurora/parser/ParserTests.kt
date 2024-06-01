package pixel.aurora.parser

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.jupiter.api.Test
import pixel.aurora.Aurora
import pixel.aurora.tokenizer.TokenBuffer
import pixel.aurora.tokenizer.Tokenizer
import java.nio.CharBuffer

class ParserTests {

    @Test
    fun `Parser Tests`() {
        val buffer = TokenBuffer(
            Tokenizer(
                CharBuffer.wrap(
                    """
                        fun i2 = 2;
                        fun i3 = 3;
                    """.trimIndent()
                )
            )
        )
        val parser = ProgramParser()
        parser.setState(Parser.State(Aurora.BLANK_URI, buffer))
        println(
            jacksonObjectMapper()
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(parser.parse())
        )
    }

}