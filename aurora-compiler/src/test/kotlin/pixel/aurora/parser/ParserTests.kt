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
                            println(args.first() is String);
                            println(args.get(0).toInt() * 2 - 2 + 3 / 4);
                            println("Hello, World!");
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
