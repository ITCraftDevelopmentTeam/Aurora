package pixel.aurora.parser

import org.junit.jupiter.api.assertDoesNotThrow
import pixel.aurora.core.parser.*
import pixel.aurora.core.parser.util.RawIdentifierParser
import pixel.aurora.core.tokenizer.TokenBuffer
import pixel.aurora.core.tokenizer.DefaultTokenizer
import pixel.aurora.core.tokenizer.IdentifierToken
import pixel.aurora.core.tokenizer.toTokenBuffer
import kotlin.test.Test

class ParserTests {

    class SimpleParser : Parser<List<Pair<IdentifierToken, Any>>>() {

        override fun parse(): List<Pair<IdentifierToken, Any>> {
            return include(part().list("{", "}", allowSeparatorEnd = true))
        }

        fun part() = parser {
            val left = include(RawIdentifierParser())
            buffer.get().expectPunctuation(':')
            val right = include(
                SimpleParser() or RawIdentifierParser().list(null, null, ".")
            )
            left to right
        }

    }

    fun tokenize(): TokenBuffer {
        return DefaultTokenizer(
            """
                /* Config */
                {
                    config: {
                        enabled: java.lang.Boolean
                    }
                }
            """.trimIndent()
        ).toTokenBuffer()
    }

    @Test
    fun test(): Unit = assertDoesNotThrow {
        val tokens = tokenize().also(::println)
        val parser = SimpleParser()
        tokens.parse(parser)
    }

}