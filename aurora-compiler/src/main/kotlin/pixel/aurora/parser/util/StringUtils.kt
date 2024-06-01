package pixel.aurora.parser.util

import pixel.aurora.tokenizer.TokenType

object StringUtils {

    fun chunkPunctuationAndIdentifier(text: String): List<Pair<String, TokenType>> {
        val chunks = mutableListOf<Pair<String, TokenType>>()
        var currentIdentifier = ""
        for (character in text) {
            if (character in 'a'..'z' || character in 'A' .. 'Z') {
                currentIdentifier += character
            } else {
                if (currentIdentifier.isNotEmpty()) chunks += currentIdentifier to TokenType.IDENTIFIER
                currentIdentifier = ""
                chunks += character.toString() to TokenType.PUNCTUATION
            }
        }
        if (currentIdentifier.isNotEmpty()) chunks += currentIdentifier to TokenType.IDENTIFIER
        return chunks
    }

}