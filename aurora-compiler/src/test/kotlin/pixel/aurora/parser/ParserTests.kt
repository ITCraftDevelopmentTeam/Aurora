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
                        package pixel.aurora;
                        
                        fun main(args: Array<String>) {
                            println(
                                "Hello: " + (args.first() is String).toInt().toString(2).hashCode() * 3 / 2 + 1 - 4 as Int
                            );
                        }
                    """.trimIndent()
                )
            )
        )
        val parser = ProgramParser()
        parser.setState(Parser.State(Aurora.BLANK_URI, buffer))
        val program = parser.parse()
        println(
            jacksonObjectMapper()
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(program)
        )
    }

}
