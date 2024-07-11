package pixel.aurora.core.parser.operator

import pixel.aurora.core.parser.AbstractParser
import pixel.aurora.core.parser.include

class RepeatingOperator<T : Any>(val element: AbstractParser<T>, val min: Int = 0, val max: Int = Int.MAX_VALUE) :
    AbstractParser<List<T>>() {

    override fun parse(): List<T> {
        val list = mutableListOf<T>()
        if (max == 0 || max < min) throw makeError("Maximum times must be greater than 0 and not less than minimum times.")
        for (number in 1..max) {
            val optional = include(element.optional())
            val result = optional.getOrNull()
            if (optional.isFailure || result == null) break
            else list += result
        }
        if (list.size < min) throw makeError("Expected at least $min times, but got ${list.size}")
        return list.toList()
    }

}

fun <T : Any> AbstractParser<T>.repeat(min: Int = 0, max: Int = Int.MAX_VALUE) = RepeatingOperator(this, min, max)
