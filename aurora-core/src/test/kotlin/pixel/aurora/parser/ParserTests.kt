package pixel.aurora.parser

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.assertDoesNotThrow
import pixel.aurora.core.parser.parse
import pixel.aurora.core.parser.rule
import pixel.aurora.core.tokenizer.*
import kotlin.test.Test

class ParserTests {

    val callExpr = rule {
        val name = identifier().getName()
        punctuation("(")
        val numeric = numeric().getNumber()
        punctuation(",")
        val boolean = +boolean()
        punctuation(");")
        Triple(name, numeric, boolean)
    }

    fun tokenize() = DefaultTokenizer(
        """
            println(1.0, true);
        """.trimIndent()
    ).toTokenBuffer()

    @Test
    fun test() {
        val result = assertDoesNotThrow {
            val tokens = tokenize()
            tokens.parse(callExpr)
        }
        assertEquals(Triple("println", 1.0.toBigDecimal(), true), result)
    }

}