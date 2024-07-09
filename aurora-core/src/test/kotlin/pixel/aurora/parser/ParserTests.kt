package pixel.aurora.parser

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.assertDoesNotThrow
import pixel.aurora.core.parser.parse
import pixel.aurora.core.parser.rule
import pixel.aurora.core.tokenizer.DefaultTokenizer
import pixel.aurora.core.tokenizer.identifier
import pixel.aurora.core.tokenizer.punctuation
import pixel.aurora.core.tokenizer.toTokenBuffer
import kotlin.test.Test

class ParserTests {

    val callExpr = rule {
        val name = identifier()
        punctuation("(")
        punctuation(");")
        name.getName()
    }

    fun tokenize() = DefaultTokenizer(
        """
            println();
        """.trimIndent()
    ).toTokenBuffer()

    @Test
    fun test(): Unit = assertDoesNotThrow {
        val tokens = tokenize()
        assertEquals("println", tokens.parse(callExpr))
    }

}