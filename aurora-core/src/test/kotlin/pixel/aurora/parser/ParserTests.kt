package pixel.aurora.parser

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.assertDoesNotThrow
import pixel.aurora.core.parser.operator.list
import pixel.aurora.core.parser.parse
import pixel.aurora.core.parser.rule
import pixel.aurora.core.tokenizer.*
import kotlin.test.Test

class ParserTests {

    val callExpr = rule {
        whitespace()
        val name = identifier().getName()
        whitespace()
        punctuation("(")
        mark(true)
        whitespace()
        val numeric = +numeric()
        whitespace()
        punctuation(",")
        whitespace()
        val boolean = +boolean()
        whitespace()
        val list = +rule(block = ::token).list(",", ")", ",", true)
        whitespace()
        punctuation(";")
        Triple(name, numeric, boolean) to list
    }

    fun tokenize() = DefaultTokenizer(
        """
            println(1.0, true, a, b, c, d, e, f, g);
        """.trimIndent()
    ).toTokenBuffer()

    @Test
    fun test() {
        val result = assertDoesNotThrow {
            val tokens = tokenize()
            tokens.parse(
                callExpr
            )
        }
        assertEquals(Triple("println", 1.0.toBigDecimal(), true), result.first)
        assertEquals(7, result.second.size)
    }

}