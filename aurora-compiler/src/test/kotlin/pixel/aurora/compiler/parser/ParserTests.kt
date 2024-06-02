package pixel.aurora.compiler.parser

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.jupiter.api.Test
import pixel.aurora.compiler.AuroraCompiler
import pixel.aurora.compiler.tokenizer.TokenBuffer
import pixel.aurora.compiler.tokenizer.Tokenizer
import java.nio.CharBuffer


class ParserTests {

    @Test
    fun `Parser Tests`() {
        val buffer = TokenBuffer(
            Tokenizer(
                CharBuffer.wrap(
                    """
                        /**
                         * Block Comment
                         */
                        
                        @[file:Program];
                        package pixel.aurora;
                        
                        import pixel.aurora.lang.Singleton;
                        import pixel.aurora.util.*; 
                        
                        @[VariableDeclaration]
                        const APPLICATION_NAME: String = "Aurora";
                        
                        const OBJECT: Any = object : Any(), Interface {
                            const type = "Expression";
                        };
                   
                        object Singleton : Any(), Interface {
                            const type = "Declaration";
                        }
                        
                        public interface A<R> : B, C, D, E {
                            type Alias<R> = (A<R>, B, C, D, E);
                            interface B
                            interface C
                            interface D
                            interface E
                            function run(): R
                        }
                        
                        @[BlockFunctionDeclaration<T>, any:Unsafe()]
                        internal function <T> cast(object: Any? = null): T {
                            return object as T;
                        }
                        
                        function i"internal/function"(args: Array<String?>, mapper: ((origin: String) -> String) = { return it; }) {
                            println(cast<String>(object = APPLICATION_NAME));
                            
                            function innerFunction() = this@i"internal/function"::class;
                            
                            result := runCatching @[LambdaExpression] {
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
        parser.setState(Parser.State(AuroraCompiler.BLANK_URI, buffer))
        val program = parser.parse()
        println(
            jacksonObjectMapper()
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(program)
        )
    }

}
