package pixel.aurora.compiler.parser.operator

import pixel.aurora.compiler.parser.Parser
import pixel.aurora.compiler.parser.includeWithState
import pixel.aurora.compiler.parser.optional

class ChoiceOperator<T : Any>(vararg val choices: Parser<out T>) : Parser<T>() {

    override fun parse(): T {
        var exception: Throwable? = null
        for (choice in choices) {
            val result = includeWithState(choice.optional())
            if (result.second.isSuccess) return result.second.getOrNull() ?: throw makeError("Invalid syntax")
            else if (result.first.isMarked) exception = result.second.exceptionOrNull()!!
        }
        throw makeError("Invalid syntax", cause = exception)
    }

}