package pixel.aurora.core.parser.operator

import pixel.aurora.core.parser.Parser
import pixel.aurora.core.parser.buffer
import pixel.aurora.core.parser.include

class OptionalOperator<T : Any>(val parser: Parser<T>, val rollback: Boolean = true) : Parser<Result<T>>() {

    override fun parse(): Result<T> {
        val position = buffer.position()
        try {
            val result = Result.success(include(parser))
            return result
        } catch (throwable: Throwable) {
            if (rollback) {
                buffer.position(position)
            }
            return Result.failure(throwable)
        }
    }

}