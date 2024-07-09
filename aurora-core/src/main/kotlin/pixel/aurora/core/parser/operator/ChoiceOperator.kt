package pixel.aurora.core.parser.operator

import pixel.aurora.core.parser.Parser
import pixel.aurora.core.parser.includeWithState

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

fun <T : Any> choose(vararg choices: Parser<out T>) = ChoiceOperator(*choices)

@Suppress("UNCHECKED_CAST")
infix fun <T : Any> Parser<out T>.or(other: Parser<out T>): ChoiceOperator<T> {
    return if (this is ChoiceOperator<*>) choose(*this.choices, other) as ChoiceOperator<T>
    else if (other is ChoiceOperator<*>) choose(this, *other.choices) as ChoiceOperator<T>
    else choose(this, other)
}

infix fun <T : Any> ChoiceOperator<T>.not(filter: (Parser<out T>) -> Boolean) =
    ChoiceOperator(*this.choices.filter(filter).toTypedArray())

inline fun <reified T : Any> ChoiceOperator<T>.not() = not {
    it !is T && !T::class.isInstance(it)
}
