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
                        
                        const APPLICATION_NAME: String = "Aurora";
                        
                        function <T : Any> main(args: Array<String?>, mapper: ((origin: String) -> String)) {
                            println(APPLICATION_NAME);
                            
                            result := runCatching {
                                return mapper(((args?.first()!! is String).toNumber().toString(2).hashCode() * 3 / 2 + 1 - 4 as Int).toString());
                            };
                            
                            closure := { it: Result<Any?> ->
                                println(result);
                                return "Closure End";
                            }(result).also(::println);
                            
                            System.exit(0);
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
